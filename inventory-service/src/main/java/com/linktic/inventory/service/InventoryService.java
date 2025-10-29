package com.linktic.inventory.service;

import com.linktic.inventory.model.Inventory;
import com.linktic.inventory.repo.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryRepository repo;

    public InventoryService(InventoryRepository repo) { this.repo = repo; }

    public long getQuantity(Long productId){
        return repo.findByProductId(productId).map(i -> i.getQuantity()==null?0L:i.getQuantity()).orElse(0L);
    }

    @Transactional
    public long setQuantity(Long productId, long newQty){
        if (newQty < 0) throw new IllegalArgumentException("INVALID_QUANTITY");
        Inventory inv = repo.findByProductId(productId).orElseGet(() -> { Inventory i = new Inventory(); i.setProductId(productId); i.setQuantity(0L); return i; });
        inv.setQuantity(newQty);
        repo.save(inv);
        return inv.getQuantity();
    }
}
