package io.github.amoghk0216.trading_backend.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record WatchlistItemDto(
        Long watchlistId,
        Long userId,
        String coinId,
        String coinName,
        String coinSymbol,
        BigDecimal priceUsd,
        BigDecimal priceChangePercentage24h,
        Instant addedTime
) {}
