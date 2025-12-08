package io.github.amoghk0216.trading_backend.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CoinGeckoApiResponseDto(
        String id,
        String symbol,
        String name,

        @JsonProperty("market_data")
        MarketData marketData,

        @JsonProperty("last_updated")
        String lastUpdated
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MarketData(
            @JsonProperty("current_price")
            Map<String, BigDecimal> currentPrice,

            @JsonProperty("price_change_percentage_24h")
            BigDecimal priceChangePercentage24h
    ){}
}
