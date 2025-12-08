package io.github.amoghk0216.trading_backend.dto;

import lombok.Data;

public record UserDto (
        String name,
        String email,
        String password
){}
