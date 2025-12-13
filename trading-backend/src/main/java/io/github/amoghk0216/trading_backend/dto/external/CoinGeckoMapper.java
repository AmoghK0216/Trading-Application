package io.github.amoghk0216.trading_backend.dto.external;

import io.github.amoghk0216.trading_backend.dto.CoinResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
public class CoinGeckoMapper {

    private static final Logger logger = LoggerFactory.getLogger(CoinGeckoMapper.class);

    public CoinResponseDto toDto(CoinGeckoApiResponseDto response){
        return toDto(response, "usd");
    }

    public CoinResponseDto toDto(CoinGeckoApiResponseDto response, String currency){
        if (response == null) {
            logger.error("Cannot map null response to CoinResponseDto");
            throw new IllegalArgumentException("Response cannot be null");
        }

        if (currency == null || currency.isBlank()) {
            logger.warn("Invalid currency provided, defaulting to 'usd'");
            currency = "usd";
        }

        try {
            var marketData = response.marketData();
            if (marketData == null) {
                logger.error("Market data is null for coin: {}", response.id());
                throw new IllegalArgumentException("Market data is missing");
            }

            if (marketData.currentPrice() == null) {
                logger.error("Current price map is null for coin: {}", response.id());
                throw new IllegalArgumentException("Price data is missing");
            }

            BigDecimal current_price = marketData.currentPrice()
                .getOrDefault(currency.trim().toLowerCase(), BigDecimal.ZERO);

            if (current_price.equals(BigDecimal.ZERO)) {
                logger.warn("Price not found for currency '{}', defaulting to ZERO for coin: {}",
                    currency, response.id());
            }

            BigDecimal priceChangePercentage24h = marketData.priceChangePercentage24h();
            if (priceChangePercentage24h == null) {
                logger.warn("24h price change percentage is null for coin: {}", response.id());
                priceChangePercentage24h = BigDecimal.ZERO;
            }

            Instant lastUpdated;
            try {
                lastUpdated = Instant.parse(response.lastUpdated());
            } catch (Exception e) {
                logger.warn("Failed to parse last updated timestamp for coin: {}, using current time",
                    response.id(), e);
                lastUpdated = Instant.now();
            }

            logger.debug("Successfully mapped coin data for: {}", response.id());
            return new CoinResponseDto(
                    response.id(),
                    response.symbol(),
                    response.name(),
                    current_price,
                    priceChangePercentage24h,
                    lastUpdated
            );

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while mapping coin data for: {}", response.id(), e);
            throw new RuntimeException("Failed to map coin data", e);
        }
    }
}
