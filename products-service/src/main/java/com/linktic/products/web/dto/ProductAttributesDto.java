package com.linktic.products.web.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductAttributesDto {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    private String name;
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio debe ser mayor o igual a 0")
    @DecimalMax(value = "1000000.0", inclusive = true, message = "El precio debe ser menor o igual a 1,000,000")
    @Digits(integer = 7, fraction = 2, message = "El precio debe tener hasta 7 dígitos enteros y 2 decimales")
    private BigDecimal price;
    @Size(max = 200, message = "La descripción debe tener máximo 200 caracteres")
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
