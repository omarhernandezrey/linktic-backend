package com.linktic.products.service;

import com.linktic.products.model.Product;
import com.linktic.products.repo.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository repo;
    public ProductService(ProductRepository repo){ this.repo = repo; }

    public Product create(String name, BigDecimal price, String description){
        if (name == null || name.isBlank()) throw new IllegalArgumentException("NAME_REQUIRED");
        if (price == null || price.signum() < 0) throw new IllegalArgumentException("INVALID_PRICE");
        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setDescription(description);
        return repo.save(p);
    }

    public Optional<Product> getById(Long id){
        return repo.findById(id);
    }

    public List<Product> findAll(){
        return repo.findAll();
    }
}
