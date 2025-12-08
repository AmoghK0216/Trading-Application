package io.github.amoghk0216.trading_backend.service;

import io.github.amoghk0216.trading_backend.client.CoinGeckoClient;
import io.github.amoghk0216.trading_backend.dto.CoinResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CoinService {
    private final CoinGeckoClient coinGeckoClient;

    public CoinService(CoinGeckoClient coinGeckoClient){
        this.coinGeckoClient = coinGeckoClient;
    }

    public CoinResponseDto getCoinById(String coinId){
        return coinGeckoClient.getCoinById(coinId);
    }

    public CoinResponseDto getCoinById(String coinId, String currency){
        return coinGeckoClient.getCoinById(coinId, currency);
    }
}