package io.github.amoghk0216.trading_backend.client;

import io.github.amoghk0216.trading_backend.dto.CoinResponseDto;
import io.github.amoghk0216.trading_backend.dto.external.CoinGeckoApiResponseDto;
import io.github.amoghk0216.trading_backend.dto.external.CoinGeckoMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class CoinGeckoClient {
    private final WebClient webClient;
    private final CoinGeckoMapper mapper;

    public CoinGeckoClient(@Qualifier("coinGeckoWebClient") WebClient webClient, CoinGeckoMapper mapper ){
        this.webClient = webClient;
        this.mapper = mapper;
    }

    public CoinResponseDto getCoinById(String coinId){
        CoinGeckoApiResponseDto response = webClient.get()
                .uri("/coins/{id}", coinId)
                .retrieve()
                .bodyToMono(CoinGeckoApiResponseDto.class)
                .block();

        return mapper.toDto(response);
    }

    public CoinResponseDto getCoinById(String coinId, String currency){
        CoinGeckoApiResponseDto response = webClient.get()
                .uri("/coins/{id}", coinId)
                .retrieve()
                .bodyToMono(CoinGeckoApiResponseDto.class)
                .block();

        return mapper.toDto(response, currency);
    }
}
