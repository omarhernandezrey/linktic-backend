package com.linktic.products.web;

import com.linktic.products.model.Product;
import com.linktic.products.repo.ProductRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/products", produces = "application/vnd.api+json")
public class ProductController {
    private final ProductRepository repo;
    public ProductController(ProductRepository repo){ this.repo = repo; }

    @PostMapping(consumes = "application/vnd.api+json")
    public ResponseEntity<?> create(@RequestBody Map<String,Object> body){
        try{
            Map<String,Object> data = (Map<String,Object>) body.get("data");
            Map<String,Object> attrs = (Map<String,Object>) data.get("attributes");
            Product p = new Product();
            p.setName((String)attrs.get("name"));
            Object price = attrs.get("price");
            p.setPrice(new BigDecimal(String.valueOf(price)));
            p.setDescription((String)attrs.getOrDefault("description", null));
            p = repo.save(p);
            return ResponseEntity.status(201).contentType(MediaType.valueOf("application/vnd.api+json"))
                    .body(JsonApi.resource("products", String.valueOf(p.getId()), Map.of(
                            "name", p.getName(), "price", p.getPrice(), "description", p.getDescription()
                    )));
        }catch(Exception ex){
            return ResponseEntity.badRequest().body(JsonApi.error(400,"Bad Request", ex.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id){
        return repo.findById(id)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(JsonApi.resource("products", String.valueOf(p.getId()), Map.of(
                        "name", p.getName(), "price", p.getPrice(), "description", p.getDescription()
                ))))
                .orElseGet(() -> ResponseEntity.status(404).body(JsonApi.error(404,"Not Found","Product "+id+" not found")));
    }

    @GetMapping
    public ResponseEntity<?> list(){
        List<Map<String,Object>> items = repo.findAll().stream().map(p -> Map.of(
                "type","products","id",String.valueOf(p.getId()),
                "attributes", Map.of("name",p.getName(),"price",p.getPrice(),"description",p.getDescription())
        )).toList();
        return ResponseEntity.ok(JsonApi.collection("products", items));
    }
}
