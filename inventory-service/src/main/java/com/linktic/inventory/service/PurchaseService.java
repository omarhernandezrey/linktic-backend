package com.linktic.inventory.service;

import com.linktic.inventory.client.ProductsClient;
import com.linktic.inventory.model.Inventory;
import com.linktic.inventory.model.Purchase;
import com.linktic.inventory.repo.InventoryRepository;
import com.linktic.inventory.repo.PurchaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class PurchaseService {
    private final InventoryRepository inventoryRepository;
    private final PurchaseRepository purchaseRepository;
    private final ProductsClient productsClient;

    public PurchaseService(InventoryRepository inventoryRepository, PurchaseRepository purchaseRepository, ProductsClient productsClient){
        this.inventoryRepository = inventoryRepository; this.purchaseRepository = purchaseRepository; this.productsClient = productsClient;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public Purchase purchase(Long productId, long qty){
        if (qty <= 0) throw new IllegalArgumentException("INVALID_QUANTITY");

        Map<String,Object> data = productsClient.getProduct(productId).block();
        Map<String,Object> dataObj = (Map<String,Object>) data.get("data");
        Map<String,Object> attrs = (Map<String,Object>) dataObj.get("attributes");
        BigDecimal unitPrice = new BigDecimal(String.valueOf(attrs.get("price")));

        Inventory inv = inventoryRepository.findByProductId(productId).orElseGet(() -> { Inventory i = new Inventory(); i.setProductId(productId); i.setQuantity(0L); return i; });
        long current = inv.getQuantity() == null ? 0L : inv.getQuantity();
        if (current < qty) throw new IllegalStateException("INSUFFICIENT_STOCK");

        inv.setQuantity(current - qty);
        inventoryRepository.save(inv);

        Purchase p = new Purchase();
        p.setProductId(productId);
        p.setQuantity(qty);
        p.setUnitPrice(unitPrice);
        p.setTotal(unitPrice.multiply(BigDecimal.valueOf(qty)));
        return purchaseRepository.save(p);
    }
}
