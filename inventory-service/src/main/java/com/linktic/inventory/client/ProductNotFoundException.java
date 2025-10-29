package com.linktic.inventory.client;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("PRODUCT_NOT_FOUND");
    }
}
