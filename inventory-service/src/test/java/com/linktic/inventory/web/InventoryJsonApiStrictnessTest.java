package com.linktic.inventory.web;

import com.linktic.inventory.service.InventoryService;
import com.linktic.inventory.service.PurchaseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InventoryController.class)
@Import(GlobalExceptionHandler.class)
class InventoryJsonApiStrictnessTest {

    @Autowired MockMvc mvc;
    @MockBean InventoryService inventoryService;
    @MockBean PurchaseService purchaseService;

    @Test
    void postPurchases_withWrongContentType_returns415JsonApi() throws Exception {
        String body = "{\n  \"data\": {\n    \"type\": \"purchases\",\n    \"attributes\": {\n      \"productId\": 1, \"quantity\": 1\n    }\n  }\n}";

        mvc.perform(post("/purchases")
                        .header("X-API-KEY","change-me")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(content().contentType("application/vnd.api+json"))
                .andExpect(jsonPath("$.errors[0].status").value("415"));
    }

    @Test
    void getInventory_withUnacceptableAccept_returns406JsonApi() throws Exception {
        Mockito.when(inventoryService.getQuantity(1L)).thenReturn(0L);
        mvc.perform(get("/inventory/1")
                        .header("X-API-KEY","change-me")
                        .accept("application/json"))
                .andExpect(status().isNotAcceptable())
                .andExpect(content().contentType("application/vnd.api+json"))
                .andExpect(jsonPath("$.errors[0].status").value("406"));
    }
}
