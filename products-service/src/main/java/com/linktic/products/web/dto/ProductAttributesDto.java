package com.linktic.products.web.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductAttributesDto {
    @NotBlank(message = "name is required")
    @Size(min = 2, max = 80, message = "name length must be between 2 and 80")
    private String name;
    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "price must be >= 0")
    @DecimalMax(value = "1000000.0", inclusive = true, message = "price must be <= 1000000")
    private BigDecimal price;
    @Size(max = 200, message = "description length must be <= 200")
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
