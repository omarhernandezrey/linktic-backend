package com.linktic.inventory.web.dto;

import jakarta.validation.constraints.*;

public class PurchaseRequestDto {
    @NotNull(message = "productId is required")
    private Long productId;
    @Min(value = 1, message = "quantity must be >= 1")
    @Max(value = 100000, message = "quantity must be <= 100000")
    private long quantity;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public long getQuantity() { return quantity; }
    public void setQuantity(long quantity) { this.quantity = quantity; }
}
