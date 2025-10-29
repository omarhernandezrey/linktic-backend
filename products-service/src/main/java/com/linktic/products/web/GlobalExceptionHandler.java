package com.linktic.products.web;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex){
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> Map.of(
                        "status", "400",
                        "title", "Bad Request",
                        "detail", fe.getDefaultMessage(),
                        "source", Map.of("pointer", toJsonApiPointer(fe.getField()))
                )).toList();
    return ResponseEntity.badRequest()
        .contentType(org.springframework.http.MediaType.valueOf("application/vnd.api+json"))
        .body(Map.of("errors", errors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArg(IllegalArgumentException ex){
        return ResponseEntity.badRequest()
                .contentType(org.springframework.http.MediaType.valueOf("application/vnd.api+json"))
                .body(JsonApi.error(400, "Bad Request", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(org.springframework.http.MediaType.valueOf("application/vnd.api+json"))
                .body(JsonApi.error(500, "Internal Server Error", ex.getMessage()));
    }

    private String toJsonApiPointer(String field){
        // field like "data.attributes.name" -> "/data/attributes/name"
        return "/" + field.replace('.', '/');
    }
}
