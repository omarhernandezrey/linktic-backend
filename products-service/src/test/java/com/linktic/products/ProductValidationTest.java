package com.linktic.products;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductValidationTest {

    @Autowired MockMvc mvc;

    @Test
    void create_withInvalidName_returnsJsonApiErrors() throws Exception {
        String body = """
          {"data":{"type":"products","attributes":{"name":"A","price":49.9,"description":"ok"}}}
        """;
        mvc.perform(post("/products")
                .contentType(MediaType.valueOf("application/vnd.api+json"))
                .header("X-API-KEY", "change-me")
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/vnd.api+json"))
                .andExpect(jsonPath("$.errors[0].source.pointer").value("/data/attributes/name"));
    }

    @Test
    void create_withInvalidPrice_returnsJsonApiErrors() throws Exception {
        String body = """
          {"data":{"type":"products","attributes":{"name":"Valid Name","price":-1,"description":"ok"}}}
        """;
        mvc.perform(post("/products")
                .contentType(MediaType.valueOf("application/vnd.api+json"))
                .header("X-API-KEY", "change-me")
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/vnd.api+json"))
                .andExpect(jsonPath("$.errors[0].source.pointer").value("/data/attributes/price"));
    }
}
