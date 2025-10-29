package com.linktic.products;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
  "spring.datasource.url=jdbc:h2:mem:products;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
  "spring.datasource.driverClassName=org.h2.Driver",
  "spring.datasource.username=sa",
  "spring.datasource.password=",
  "spring.jpa.hibernate.ddl-auto=create-drop",
  "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
  "INTERSERVICE_API_KEY=change-me"
})
class ProductControllerTest {
    @Autowired MockMvc mvc;

    @Test
    void createAndGet() throws Exception {
        String body = """
          {"data":{"type":"products","attributes":{"name":"Mouse","price":49.9,"description":"Inalámbrico"}}}
        """;
  String apiKey = "change-me";
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
