package com.linktic.inventory.web;

import com.linktic.inventory.client.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

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
                .contentType(MediaType.valueOf("application/vnd.api+json"))
                .body(Map.of("errors", errors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArg(IllegalArgumentException ex){
        return ResponseEntity.badRequest()
                .contentType(MediaType.valueOf("application/vnd.api+json"))
                .body(JsonApi.error(400, "Bad Request", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleState(IllegalStateException ex){
        return ResponseEntity.status(409)
                .contentType(MediaType.valueOf("application/vnd.api+json"))
                .body(JsonApi.error(409, "Conflict", ex.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> handleProductNotFound(ProductNotFoundException ex){
        return ResponseEntity.status(404)
                .contentType(MediaType.valueOf("application/vnd.api+json"))
                .body(JsonApi.error(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex){
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .contentType(MediaType.valueOf("application/vnd.api+json"))
                .body(JsonApi.error(415, "Unsupported Media Type", "Content-Type debe ser application/vnd.api+json"));
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<?> handleNotAcceptable(HttpMediaTypeNotAcceptableException ex){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.valueOf("application/vnd.api+json"))
                .body(JsonApi.error(406, "Not Acceptable", "Accept debe incluir application/vnd.api+json"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.valueOf("application/vnd.api+json"))
                .body(JsonApi.error(500, "Internal Server Error", ex.getMessage()));
    }

    private String toJsonApiPointer(String field){
        return "/" + field.replace('.', '/');
    }
}
