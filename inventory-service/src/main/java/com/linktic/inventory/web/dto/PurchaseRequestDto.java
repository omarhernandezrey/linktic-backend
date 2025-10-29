package com.linktic.inventory.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PurchaseRequestDto {
    @NotNull(message = "productId is required")
    private Long productId;
    @Min(value = 1, message = "quantity must be >= 1")
    private long quantity;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public long getQuantity() { return quantity; }
    public void setQuantity(long quantity) { this.quantity = quantity; }
}
