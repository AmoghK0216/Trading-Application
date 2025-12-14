package io.github.amoghk0216.trading_backend.dto;

public record CoinSearchResultDto(
        String id,
        String name,
        String symbol,
        Integer marketCapRank,
        String imageUrl
) {}
