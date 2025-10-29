package com.linktic.products;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:products_strict;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
  "spring.jpa.hibernate.ddl-auto=create-drop",
        // Activar filtro API Key para probar 401
        "api.key=my-secret"
})
class JsonApiStrictnessTest {

    @Autowired MockMvc mvc;

    private static final String VND = "application/vnd.api+json";

    @Test
    @DisplayName("POST /products con Content-Type incorrecto debe responder 415")
    void wrongContentType_returns415() throws Exception {
        String body = """
          {"data":{"type":"products","attributes":{"name":"Mouse","price":49.9}}}
        """;
        mvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-KEY", "my-secret")
                .content(body))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @DisplayName("POST /products sin X-API-KEY debe responder 401 JSON:API")
    void missingApiKey_returns401() throws Exception {
        String body = """
          {"data":{"type":"products","attributes":{"name":"Mouse","price":49.9}}}
        """;
        mvc.perform(post("/products")
                .contentType(VND)
                .content(body))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(VND))
                .andExpect(jsonPath("$.errors[0].status").value("401"));
    }

    @Test
    @DisplayName("POST /products con type distinto a 'products' debe responder 409 JSON:API")
    void wrongType_returns409() throws Exception {
        String body = """
          {"data":{"type":"items","attributes":{"name":"Mouse","price":49.9}}}
        """;
        mvc.perform(post("/products")
                .contentType(VND)
                .header("X-API-KEY", "my-secret")
                .content(body))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(VND))
                .andExpect(jsonPath("$.errors[0].status").value("409"));
    }

    @Test
    @DisplayName("POST /products con 'id' en el payload debe responder 400 JSON:API")
    void idProvidedOnCreate_returns400() throws Exception {
        String body = """
          {"data":{"type":"products","id":"123","attributes":{"name":"Mouse","price":49.9}}}
        """;
        mvc.perform(post("/products")
                .contentType(VND)
                .header("X-API-KEY", "my-secret")
                .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(VND))
                .andExpect(jsonPath("$.errors[0].status").value("400"));
    }
}
