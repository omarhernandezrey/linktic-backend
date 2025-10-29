package com.linktic.inventory.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex){
        String msg = ex.getBindingResult().getAllErrors().stream().findFirst().map(e-> e.getDefaultMessage()).orElse("Validation error");
        return ResponseEntity.badRequest().body(JsonApi.error(400, "Bad Request", msg));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArg(IllegalArgumentException ex){
        return ResponseEntity.badRequest().body(JsonApi.error(400, "Bad Request", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleState(IllegalStateException ex){
        return ResponseEntity.status(409).body(JsonApi.error(409, "Conflict", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(JsonApi.error(500, "Internal Server Error", ex.getMessage()));
    }
}
