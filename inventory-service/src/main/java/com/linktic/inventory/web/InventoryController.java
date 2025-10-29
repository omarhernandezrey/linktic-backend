package com.linktic.inventory.web;

import com.linktic.inventory.model.Purchase;
import com.linktic.inventory.service.InventoryService;
import com.linktic.inventory.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/", produces = "application/vnd.api+json")
public class InventoryController {
    private final PurchaseService purchaseService;
    private final InventoryService inventoryService;

    public InventoryController(PurchaseService purchaseService, InventoryService inventoryService){ this.purchaseService = purchaseService; this.inventoryService = inventoryService; }

    @GetMapping
    public ResponseEntity<?> root() {
        return ResponseEntity.ok(Map.of("message", "Inventory Service is running"));
    }

    @GetMapping("inventory/{productId}")
    public ResponseEntity<?> getQty(@PathVariable("productId") Long productId){
        long qty = inventoryService.getQuantity(productId);
        return ResponseEntity.ok(JsonApi.resource("inventory", String.valueOf(productId), Map.of("quantity", qty)));
    }

    @PatchMapping(value="inventory/{productId}", consumes = "application/vnd.api+json")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> setQty(@PathVariable("productId") Long productId, @RequestBody Map<String,Object> body){
        Map<String,Object> data = (Map<String,Object>) body.get("data");
        Map<String,Object> attrs = (Map<String,Object>) data.get("attributes");
        long newQty = Long.parseLong(String.valueOf(attrs.get("quantity")));
        long updated = inventoryService.setQuantity(productId, newQty);
        return ResponseEntity.ok(JsonApi.resource("inventory", String.valueOf(productId), Map.of("quantity", updated)));
    }

    @PostMapping(value="purchases", consumes = "application/vnd.api+json")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> purchase(@RequestBody Map<String,Object> body){
            Map<String,Object> data = (Map<String,Object>) body.get("data");
            Map<String,Object> attrs = (Map<String,Object>) data.get("attributes");
            Long productId = Long.parseLong(String.valueOf(attrs.get("productId")));
            long qty = Long.parseLong(String.valueOf(attrs.get("quantity")));
            Purchase p = purchaseService.purchase(productId, qty);
            return ResponseEntity.status(201).body(JsonApi.resource("purchases", String.valueOf(p.getId()), Map.of(
                    "productId", p.getProductId(), "quantity", p.getQuantity(), "unitPrice", p.getUnitPrice(), "total", p.getTotal(), "createdAt", p.getCreatedAt().toString()
            )));
    }
}
