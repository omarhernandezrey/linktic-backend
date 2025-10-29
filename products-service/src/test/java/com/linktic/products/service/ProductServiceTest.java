package com.linktic.products.service;

import com.linktic.products.model.Product;
import com.linktic.products.repo.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    @Test
    void create_validatesInputs(){
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        ProductService svc = new ProductService(repo);
        assertThrows(IllegalArgumentException.class, () -> svc.create("", BigDecimal.ONE, null));
        assertThrows(IllegalArgumentException.class, () -> svc.create("a", new BigDecimal("-1"), null));
    }

    @Test
    void create_savesProduct(){
        ProductRepository repo = Mockito.mock(ProductRepository.class);
        Mockito.when(repo.save(Mockito.any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        ProductService svc = new ProductService(repo);
        Product p = svc.create("N", BigDecimal.TEN, "d");
        assertEquals("N", p.getName());
        assertEquals(BigDecimal.TEN, p.getPrice());
    }
}
