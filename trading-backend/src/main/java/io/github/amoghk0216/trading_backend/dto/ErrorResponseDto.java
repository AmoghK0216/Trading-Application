package io.github.amoghk0216.trading_backend.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
    public ErrorResponseDto(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path);
    }
}

