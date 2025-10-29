package com.linktic.inventory.web.dto;

import jakarta.validation.constraints.Min;

public class InventoryQuantityDto {
    @Min(value = 0, message = "quantity must be >= 0")
    private long quantity;

    public long getQuantity() { return quantity; }
    public void setQuantity(long quantity) { this.quantity = quantity; }
}
