package com.linktic.inventory.web;

import com.linktic.inventory.model.Inventory;
import com.linktic.inventory.model.Purchase;
import com.linktic.inventory.repo.InventoryRepository;
import com.linktic.inventory.service.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/", produces = "application/vnd.api+json")
public class InventoryController {
    private final InventoryRepository repo;
    private final PurchaseService purchaseService;

    public InventoryController(InventoryRepository repo, PurchaseService purchaseService){ this.repo = repo; this.purchaseService = purchaseService; }

    @GetMapping("inventory/{productId}")
    public ResponseEntity<?> getQty(@PathVariable Long productId){
        var inv = repo.findByProductId(productId).orElse(null);
        long qty = inv==null?0L:inv.getQuantity();
        return ResponseEntity.ok(JsonApi.resource("inventory", String.valueOf(productId), Map.of("quantity", qty)));
    }

    @PatchMapping(value="inventory/{productId}", consumes = "application/vnd.api+json")
    public ResponseEntity<?> setQty(@PathVariable Long productId, @RequestBody Map<String,Object> body){
        Map<String,Object> data = (Map<String,Object>) body.get("data");
        Map<String,Object> attrs = (Map<String,Object>) data.get("attributes");
        long newQty = Long.parseLong(String.valueOf(attrs.get("quantity")));
        var inv = repo.findByProductId(productId).orElseGet(()->{ var i=new Inventory(); i.setProductId(productId); i.setQuantity(0L); return i; });
        inv.setQuantity(newQty);
        repo.save(inv);
        return ResponseEntity.ok(JsonApi.resource("inventory", String.valueOf(productId), Map.of("quantity", inv.getQuantity())));
    }

    @PostMapping(value="purchases", consumes = "application/vnd.api+json")
    public ResponseEntity<?> purchase(@RequestBody Map<String,Object> body){
        try{
            Map<String,Object> data = (Map<String,Object>) body.get("data");
            Map<String,Object> attrs = (Map<String,Object>) data.get("attributes");
            Long productId = Long.parseLong(String.valueOf(attrs.get("productId")));
            long qty = Long.parseLong(String.valueOf(attrs.get("quantity")));
            Purchase p = purchaseService.purchase(productId, qty);
            return ResponseEntity.status(201).body(JsonApi.resource("purchases", String.valueOf(p.getId()), Map.of(
                    "productId", p.getProductId(), "quantity", p.getQuantity(), "unitPrice", p.getUnitPrice(), "total", p.getTotal(), "createdAt", p.getCreatedAt().toString()
            )));
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(JsonApi.error(400,"Bad Request", e.getMessage()));
        }catch(IllegalStateException e){
            return ResponseEntity.status(409).body(JsonApi.error(409,"Conflict", e.getMessage()));
        }catch(Exception e){
            return ResponseEntity.status(502).body(JsonApi.error(502,"Bad Gateway", e.getMessage()));
        }
    }
}
