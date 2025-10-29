package com.linktic.inventory.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public class InventoryQuantityDto {
    @Min(value = 0, message = "quantity must be >= 0")
    @Max(value = 1000000, message = "quantity must be <= 1000000")
    private long quantity;

    public long getQuantity() { return quantity; }
    public void setQuantity(long quantity) { this.quantity = quantity; }
}
