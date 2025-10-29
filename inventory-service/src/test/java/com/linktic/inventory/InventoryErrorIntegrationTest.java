package com.linktic.inventory;

import com.linktic.inventory.client.ProductNotFoundException;
import com.linktic.inventory.client.ProductsClient;
import com.linktic.inventory.model.Inventory;
import com.linktic.inventory.repo.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class InventoryErrorIntegrationTest {

    @Autowired WebTestClient webClient;
    @Autowired InventoryRepository inventoryRepository;
    @MockBean ProductsClient productsClient;

    @BeforeEach
    void setup(){
        inventoryRepository.deleteAll();
    }

    @Test
    void purchase_whenProductNotFound_returns404JsonApi() {
        Mockito.when(productsClient.getProduct(777L)).thenReturn(Mono.error(new ProductNotFoundException()));

        String body = "{" +
                "\"data\":{" +
                "\"type\":\"purchases\",\"attributes\":{\"productId\":777,\"quantity\":1}}}";

        webClient.post().uri("/purchases")
                .header("X-API-KEY","change-me")
                .contentType(MediaType.valueOf("application/vnd.api+json"))
                .bodyValue(body)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType("application/vnd.api+json")
                .expectBody()
                .jsonPath("$.errors[0].status").isEqualTo("404");
    }

    @Test
    void purchase_whenInsufficientStock_returns409JsonApi() {
        // Producto existe
        Map<String,Object> prod = Map.of("data", Map.of("attributes", Map.of("price", "10.0")));
        Mockito.when(productsClient.getProduct(88L)).thenReturn(Mono.just(prod));

        // Inventario insuficiente (stock 0)
        Inventory inv = new Inventory(); inv.setProductId(88L); inv.setQuantity(0L); inventoryRepository.save(inv);

        String body = "{" +
                "\"data\":{" +
                "\"type\":\"purchases\",\"attributes\":{\"productId\":88,\"quantity\":5}}}";

        webClient.post().uri("/purchases")
                .header("X-API-KEY","change-me")
                .contentType(MediaType.valueOf("application/vnd.api+json"))
                .bodyValue(body)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectHeader().contentType("application/vnd.api+json")
                .expectBody()
                .jsonPath("$.errors[0].status").isEqualTo("409")
                .jsonPath("$.errors[0].detail").isEqualTo("INSUFFICIENT_STOCK");
    }

    @Test
    void anyEndpoint_withoutApiKey_returns401JsonApi() {
    webClient.get().uri("/inventory/1")
        .accept(MediaType.valueOf("application/vnd.api+json"))
        .exchange()
        .expectStatus().isUnauthorized()
        .expectHeader().contentTypeCompatibleWith("application/vnd.api+json")
        .expectBody()
                .jsonPath("$.errors[0].status").isEqualTo("401");
    }
}
