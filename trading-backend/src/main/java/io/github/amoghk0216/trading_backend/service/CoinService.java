package io.github.amoghk0216.trading_backend.service;

import io.github.amoghk0216.trading_backend.client.CoinGeckoClient;
import io.github.amoghk0216.trading_backend.dto.CoinResponseDto;
import io.github.amoghk0216.trading_backend.dto.CoinSearchResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<CoinSearchResultDto> searchCoins(String query){
        logger.info("Service: Fetching search coins response for query: {}", query);
        try{
            List<CoinSearchResultDto> coins = coinGeckoClient.searchCoins(query);
            logger.info("Service: Successfully fetched search coins response");
            return coins;
        }catch (Exception e){
            logger.error("unable to retrieve coins for query: {}", query);
            throw e;
        }
    }
}