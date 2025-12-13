package io.github.amoghk0216.trading_backend.service;

import io.github.amoghk0216.trading_backend.client.CoinGeckoClient;
import io.github.amoghk0216.trading_backend.dto.CoinResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CoinService {

    private static final Logger logger = LoggerFactory.getLogger(CoinService.class);
    private final CoinGeckoClient coinGeckoClient;

    public CoinService(CoinGeckoClient coinGeckoClient){
        this.coinGeckoClient = coinGeckoClient;
    }

    public CoinResponseDto getCoinById(String coinId){
        logger.info("Service: Fetching coin data for coinId: {}", coinId);

        try {
            CoinResponseDto coin = coinGeckoClient.getCoinById(coinId);
            logger.info("Service: Successfully retrieved coin data for coinId: {}", coinId);
            return coin;
        } catch (Exception e) {
            logger.error("Service: Failed to fetch coin data for coinId: {}", coinId, e);
            throw e;
        }
    }

    public CoinResponseDto getCoinById(String coinId, String currency){
        logger.info("Service: Fetching coin data for coinId: {} with currency: {}", coinId, currency);

        try {
            CoinResponseDto coin = coinGeckoClient.getCoinById(coinId, currency);
            logger.info("Service: Successfully retrieved coin data for coinId: {} with currency: {}",
                coinId, currency);
            return coin;
        } catch (Exception e) {
            logger.error("Service: Failed to fetch coin data for coinId: {} with currency: {}",
                coinId, currency, e);
            throw e;
        }
    }
}