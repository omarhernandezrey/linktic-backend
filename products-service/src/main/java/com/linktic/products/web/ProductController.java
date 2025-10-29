package com.linktic.products.web;

import com.linktic.products.model.Product;
import com.linktic.products.service.ProductService;
import com.linktic.products.web.dto.JsonApiRequest;
import com.linktic.products.web.dto.ProductAttributesDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/products", produces = "application/vnd.api+json")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service){ this.service = service; }

    @PostMapping(consumes = "application/vnd.api+json")
    public ResponseEntity<?> create(@RequestBody @jakarta.validation.Valid JsonApiRequest<ProductAttributesDto> body){
        try{
            var vnd = MediaType.valueOf("application/vnd.api+json");
            var data = body.getData();
            // Validación estricta JSON:API
            if (!"products".equals(data.getType())) {
                return ResponseEntity.status(409)
                        .contentType(vnd)
                        .body(JsonApi.error(409, "Conflict", "El campo 'type' debe ser 'products'"));
            }
            if (data.getId() != null && !data.getId().isBlank()) {
                return ResponseEntity.badRequest()
                        .contentType(vnd)
                        .body(JsonApi.error(400, "Bad Request", "No se debe enviar 'id' en creación"));
            }

            ProductAttributesDto attrs = data.getAttributes();
            Product p = service.create(attrs.getName(), attrs.getPrice(), attrs.getDescription());
            return ResponseEntity.status(201).contentType(vnd)
                    .body(JsonApi.resource("products", String.valueOf(p.getId()), Map.of(
                            "name", p.getName(), "price", p.getPrice(), "description", p.getDescription()
                    )));
        }catch(Exception ex){
            return ResponseEntity.badRequest()
                    .contentType(MediaType.valueOf("application/vnd.api+json"))
                    .body(JsonApi.error(400,"Bad Request", ex.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") Long id){
    return service.getById(id)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(JsonApi.resource("products", String.valueOf(p.getId()), Map.of(
                        "name", p.getName(), "price", p.getPrice(), "description", p.getDescription()
                ))))
                .orElseGet(() -> ResponseEntity.status(404).body(JsonApi.error(404,"Not Found","Product "+id+" not found")));
    }

    @GetMapping
    public ResponseEntity<?> list(){
    List<Map<String,Object>> items = service.findAll().stream().map(p -> Map.of(
                "type","products","id",String.valueOf(p.getId()),
                "attributes", Map.of("name",p.getName(),"price",p.getPrice(),"description",p.getDescription())
        )).toList();
        return ResponseEntity.ok(JsonApi.collection("products", items));
    }
}
