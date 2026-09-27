package com.hdfclife.ledger.config;

import com.hdfclife.ledger.dto.ErrorResponse;
import com.hdfclife.ledger.exception.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException e) {
        Map<String, String> f = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(x -> f.putIfAbsent(x.getField(), x.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ErrorResponse(400, "Bad Request", "Validation failed", f));
    }

    @ExceptionHandler({PolicyNotFoundException.class, ClaimNotFoundException.class})
    ResponseEntity<ErrorResponse> notFound(DeskException e) {
        return body(HttpStatus.NOT_FOUND, "Not Found", e.getMessage());
    }

    @ExceptionHandler(DuplicatePolicyException.class)
    ResponseEntity<ErrorResponse> duplicate(DuplicatePolicyException e) {
        return body(HttpStatus.CONFLICT, "Conflict", e.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    ResponseEntity<ErrorResponse> invalid(InvalidRequestException e) {
        return body(HttpStatus.BAD_REQUEST, "Bad Request", e.getMessage());
    }

    private ResponseEntity<ErrorResponse> body(HttpStatus s, String error, String message) {
        return ResponseEntity.status(s).body(new ErrorResponse(s.value(), error, message, Map.of()));
    }
}
