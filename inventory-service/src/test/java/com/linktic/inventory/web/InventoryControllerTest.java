package com.linktic.inventory.web;

import com.linktic.inventory.service.InventoryService;
import com.linktic.inventory.service.PurchaseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InventoryController.class)
class InventoryControllerTest {
    @Autowired MockMvc mvc;
    @MockBean InventoryService inventoryService;
    @MockBean PurchaseService purchaseService;

    @Test
    void patchSetQty_ok() throws Exception {
        Mockito.when(inventoryService.setQuantity(1L, 5L)).thenReturn(5L);
        String body = """
        {"data":{"type":"inventory","attributes":{"quantity":5}}}
        """;
        mvc.perform(patch("/inventory/1").header("X-API-KEY","test")
                .contentType("application/vnd.api+json")
                .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.api+json"));
    }
}
