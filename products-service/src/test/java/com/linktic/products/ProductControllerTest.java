package com.linktic.products;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired MockMvc mvc;

    @Test
    void createAndGet() throws Exception {
        String body = """
          {"data":{"type":"products","attributes":{"name":"Mouse","price":49.9,"description":"Inalámbrico"}}}
        """;
        String apiKey = "super-secret-key";
        mvc.perform(post("/products")
                .contentType("application/vnd.api+json")
                .header("X-API-KEY", apiKey)
                .content(body))
                .andExpect(status().isCreated());

        mvc.perform(get("/products/1").header("X-API-KEY", apiKey))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/vnd.api+json"));
    }
}
