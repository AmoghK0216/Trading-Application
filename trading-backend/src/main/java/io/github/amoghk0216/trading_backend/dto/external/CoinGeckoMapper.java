package io.github.amoghk0216.trading_backend.dto.external;

import io.github.amoghk0216.trading_backend.dto.CoinResponseDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class CoinGeckoMapper {
    public CoinResponseDto toDto(CoinGeckoApiResponseDto response){
        return toDto(response, "usd");
    }

    public CoinResponseDto toDto(CoinGeckoApiResponseDto response, String currency){
        var marketData = response.marketData();

        BigDecimal current_price = marketData.currentPrice().getOrDefault(currency.trim().toLowerCase(), BigDecimal.ZERO);
        BigDecimal priceChangePercentage24h = marketData.priceChangePercentage24h();

        return new CoinResponseDto(
                response.id(),
                response.symbol(),
                response.name(),
                current_price,
                priceChangePercentage24h,
                Instant.parse(response.lastUpdated())
        );
    }
}
