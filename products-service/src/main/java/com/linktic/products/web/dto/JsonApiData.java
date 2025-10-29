package com.linktic.products.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class JsonApiData<T> {
    @NotBlank
    private String type;
    @NotNull @Valid
    private T attributes;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public T getAttributes() { return attributes; }
    public void setAttributes(T attributes) { this.attributes = attributes; }
}
