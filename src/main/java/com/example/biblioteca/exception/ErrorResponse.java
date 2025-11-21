package com.example.biblioteca.exception;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
    Instant timestamp,
    String path,
    Integer status,
    String error,
    String message,
    List<String> details
) {
}
