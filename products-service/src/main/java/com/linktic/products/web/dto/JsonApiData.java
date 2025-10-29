package com.linktic.products.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class JsonApiData<T> {
    @NotBlank
    private String type;
    // En POST no se debe enviar id; lo aceptamos para validarlo y rechazarlo explícitamente
    private String id;
    @NotNull @Valid
    private T attributes;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public T getAttributes() { return attributes; }
    public void setAttributes(T attributes) { this.attributes = attributes; }
}
