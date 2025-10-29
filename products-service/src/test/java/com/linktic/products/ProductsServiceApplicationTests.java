package com.linktic.products;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
    @ActiveProfiles("test") // This line is already present in the original file
class ProductsServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
