package io.github.amoghk0216.trading_backend.client;

import io.github.amoghk0216.trading_backend.dto.CoinResponseDto;
import io.github.amoghk0216.trading_backend.dto.CoinSearchResultDto;
import io.github.amoghk0216.trading_backend.dto.external.CoinGeckoApiResponseDto;
import io.github.amoghk0216.trading_backend.dto.external.CoinGeckoMapper;
import io.github.amoghk0216.trading_backend.dto.external.CoinGeckoSearchResponse;
import io.github.amoghk0216.trading_backend.exception.CoinNotFoundException;
import io.github.amoghk0216.trading_backend.exception.ExternalApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CoinGeckoClient {

    private static final Logger logger = LoggerFactory.getLogger(CoinGeckoClient.class);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    private final WebClient webClient;
    private final CoinGeckoMapper mapper;

    public CoinGeckoClient(@Qualifier("coinGeckoWebClient") WebClient webClient, CoinGeckoMapper mapper ){
        this.webClient = webClient;
        this.mapper = mapper;
    }

    public CoinResponseDto getCoinById(String coinId){
        return getCoinById(coinId, "usd");
    }

    public CoinResponseDto getCoinById(String coinId, String currency){
        logger.info("Fetching coin data for coinId: {} with currency: {}", coinId, currency);

        if (coinId == null || coinId.isBlank()) {
            logger.warn("Invalid coin ID provided: null or empty");
            throw new IllegalArgumentException("Coin ID cannot be null or empty");
        }

        if (currency == null || currency.isBlank()) {
            logger.warn("Invalid currency provided, using default 'usd'");
            currency = "usd";
        }

        try {
            CoinGeckoApiResponseDto response = webClient.get()
                    .uri("/coins/{id}", coinId)
                    .retrieve()
                    .onStatus(
                        status -> status.value() == 404,
                        clientResponse -> {
                            logger.warn("Coin not found: {}", coinId);
                            return Mono.error(new CoinNotFoundException("Coin with ID '" + coinId + "' not found"));
                        }
                    )
                    .onStatus(
                        status -> status.value() == 429,
                        clientResponse -> {
                            logger.error("Rate limit exceeded for CoinGecko API");
                            return Mono.error(new ExternalApiException("Rate limit exceeded. Please try again later."));
                        }
                    )
                    .onStatus(
                        status -> status.is5xxServerError(),
                        clientResponse -> {
                            logger.error("CoinGecko API server error for coinId: {}", coinId);
                            return Mono.error(new ExternalApiException("External service is currently unavailable"));
                        }
                    )
                    .bodyToMono(CoinGeckoApiResponseDto.class)
                    .timeout(REQUEST_TIMEOUT)
                    .block();

            if (response == null) {
                logger.error("Received null response from CoinGecko API for coinId: {}", coinId);
                throw new ExternalApiException("No data received from external service");
            }

            logger.info("Successfully fetched coin data for coinId: {} with currency: {}", coinId, currency);
            return mapper.toDto(response, currency);

        } catch (CoinNotFoundException | ExternalApiException e) {
            throw e;
        } catch (WebClientResponseException e) {
            logger.error("WebClient error while fetching coin {}: Status {}, Body: {}",
                coinId, e.getStatusCode(), e.getResponseBodyAsString());
            throw new ExternalApiException("Failed to fetch coin data: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching coin data for coinId: {} with currency: {}",
                coinId, currency, e);
            throw new ExternalApiException("An unexpected error occurred while fetching coin data", e);
        }
    }

    public List<CoinSearchResultDto> searchCoins(String query){
        logger.info("searching coins for query: {}", query);

        try{
            CoinGeckoSearchResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search")
                            .queryParam("query", query)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                            Mono.error(new ExternalApiException("CoinGecko API Unavailable")))
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                            Mono.error(new ExternalApiException("Search Failed: "+clientResponse.statusCode())))
                    .bodyToMono(CoinGeckoSearchResponse.class)
                    .timeout(REQUEST_TIMEOUT)
                    .block();

            if(response == null || response.coins() == null || response.coins().isEmpty()){
                logger.info("No coins for search query {}", query);
                return Collections.emptyList();
            }

            return response.coins().stream().map(coin -> new CoinSearchResultDto(
                    coin.id(),
                    coin.name(),
                    coin.symbol(),
                    coin.marketCapRank(),
                    coin.thumb()
            )).toList();
        }catch(Exception e){
            logger.error("Coin Search Failed: {}", e.getMessage());
            throw e;
        }
    }

    public List<CoinResponseDto> getCoinsByIds(List<String> coinIds){
        logger.info("fetching coins for: {}", coinIds);
        if(coinIds == null || coinIds.isEmpty())
            return Collections.emptyList();

        String idsParam = String.join(",", coinIds);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/coins/markets")
                        .queryParam("vs_currency", "usd")
                        .queryParam("ids", idsParam)
                        .queryParam("order", "market_cap_desc")
                        .queryParam("sparkline", "false")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalApiException("CoinGecko API Unavailable")))
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new ExternalApiException("Search Failed: "+clientResponse.statusCode())))
                .bodyToFlux(CoinResponseDto.class)
                .collectList()
                .timeout(REQUEST_TIMEOUT)
                .block();
    }
}
