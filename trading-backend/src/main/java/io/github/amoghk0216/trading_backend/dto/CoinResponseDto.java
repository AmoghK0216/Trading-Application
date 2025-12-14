package io.github.amoghk0216.trading_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

public record CoinResponseDto(
    String id,
    String symbol,
    String name,

    @JsonProperty("current_price")
    BigDecimal priceUsd,

    @JsonProperty("price_change_percentage_24h")
    BigDecimal priceChangePercentage24h,
    Instant timestamp
) {}
