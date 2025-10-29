package com.linktic.inventory;

import com.linktic.inventory.client.ProductsClient;
import com.linktic.inventory.model.Inventory;
import com.linktic.inventory.model.Purchase;
import com.linktic.inventory.repo.InventoryRepository;
import com.linktic.inventory.repo.PurchaseRepository;
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

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class InventoryPurchaseIntegrationTest {

    @Autowired WebTestClient webClient;
    @Autowired InventoryRepository inventoryRepository;
    @Autowired PurchaseRepository purchaseRepository;
    @MockBean ProductsClient productsClient;

    @BeforeEach
    void setup(){
        purchaseRepository.deleteAll();
        inventoryRepository.deleteAll();
        Inventory inv = new Inventory(); inv.setProductId(99L); inv.setQuantity(10L); inventoryRepository.save(inv);
    }

    @Test
    void purchaseFlow_success() {
        // Mock product response with price
        Map<String,Object> prod = Map.of("data", Map.of("attributes", Map.of("price", "12.5")));
        Mockito.when(productsClient.getProduct(99L)).thenReturn(Mono.just(prod));

        String body = "{" +
                "\"data\":{\"type\":\"purchases\",\"attributes\":{\"productId\":99,\"quantity\":2}}}";

    webClient.post().uri("/purchases")
        .header("X-API-KEY","change-me")
                .contentType(MediaType.valueOf("application/vnd.api+json"))
                .bodyValue(body)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType("application/vnd.api+json")
                .expectBody().consumeWith(resp -> {
                    // verify inventory decreased and purchase recorded
                    Inventory inv = inventoryRepository.findByProductId(99L).orElseThrow();
                    assertThat(inv.getQuantity()).isEqualTo(8L);
                    assertThat(purchaseRepository.findAll()).isNotEmpty();
                });
    }
}
