package com.linktic.productsservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.linktic.products.ProductsServiceApplication;

@SpringBootTest(classes = ProductsServiceApplication.class)
@ActiveProfiles("test")
class ProductsServiceContext2Tests {

    @Test
    void contextLoads() {
    }
}
