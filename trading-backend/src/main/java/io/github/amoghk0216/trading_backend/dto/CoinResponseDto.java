package io.github.amoghk0216.trading_backend.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record CoinResponseDto(
    String id,
    String symbol,
    String name,
    BigDecimal priceUsd,
    BigDecimal priceChangePercentage24h,
    Instant timestamp
) {}
