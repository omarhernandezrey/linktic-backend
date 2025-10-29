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

@Component
public class ProductsClient {
    private final WebClient webClient;
    private final long timeoutMs;

    public ProductsClient(@Value("${products.base-url}") String baseUrl,
                          @Value("${api.key}") String apiKey,
                          @Value("${HTTP_CLIENT_TIMEOUT_MS:1500}") long timeoutMs) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/vnd.api+json")
                .defaultHeader("X-API-KEY", apiKey)
                .build();
        this.timeoutMs = timeoutMs;
    }

    public Mono<Map<String,Object>> getProduct(Long id){
        return webClient.get().uri("/products/{id}", id)
                .accept(MediaType.valueOf("application/vnd.api+json"))
                .retrieve()
                .onStatus(s -> s.value()==404, resp -> Mono.error(new RuntimeException("PRODUCT_NOT_FOUND")))
                .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>() {})
                .timeout(Duration.ofMillis(this.timeoutMs));
    }
}
