package com.linktic.inventory.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.core.ParameterizedTypeReference;

import java.time.Duration;
import java.util.Map;
import reactor.util.retry.Retry;

@Component
public class ProductsClient {
    private final WebClient webClient;
    private final long timeoutMs;
    private final int maxRetries;

    public ProductsClient(@Value("${products.base-url}") String baseUrl,
                          @Value("${api.key}") String apiKey,
                          @Value("${HTTP_CLIENT_TIMEOUT_MS:1500}") long timeoutMs,
                          @Value("${HTTP_CLIENT_MAX_RETRIES:2}") int maxRetries) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.api+json")
                .defaultHeader("X-API-KEY", apiKey)
                .build();
        this.timeoutMs = timeoutMs;
        this.maxRetries = maxRetries;
    }

    public Mono<Map<String,Object>> getProduct(Long id){
        return webClient.get().uri("/products/{id}", id)
                .accept(MediaType.valueOf("application/vnd.api+json"))
                .retrieve()
                .onStatus(s -> s.value()==404, resp -> Mono.error(new RuntimeException("PRODUCT_NOT_FOUND")))
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
                .timeout(Duration.ofMillis(this.timeoutMs))
                .retryWhen(Retry.fixedDelay(this.maxRetries, Duration.ofMillis(200))
                        .filter(ex -> !(ex instanceof RuntimeException && "PRODUCT_NOT_FOUND".equals(ex.getMessage()))));
    }
}
