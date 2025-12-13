package io.github.amoghk0216.trading_backend.client;

import io.github.amoghk0216.trading_backend.dto.CoinResponseDto;
import io.github.amoghk0216.trading_backend.dto.external.CoinGeckoApiResponseDto;
import io.github.amoghk0216.trading_backend.dto.external.CoinGeckoMapper;
import io.github.amoghk0216.trading_backend.exception.CoinNotFoundException;
import io.github.amoghk0216.trading_backend.exception.ExternalApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

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
        logger.info("Fetching coin data for coinId: {}", coinId);

        if (coinId == null || coinId.isBlank()) {
            logger.warn("Invalid coin ID provided: null or empty");
            throw new IllegalArgumentException("Coin ID cannot be null or empty");
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

            logger.info("Successfully fetched coin data for coinId: {}", coinId);
            return mapper.toDto(response);

        } catch (CoinNotFoundException | ExternalApiException e) {
            throw e;
        } catch (WebClientResponseException e) {
            logger.error("WebClient error while fetching coin {}: Status {}, Body: {}",
                coinId, e.getStatusCode(), e.getResponseBodyAsString());
            throw new ExternalApiException("Failed to fetch coin data: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching coin data for coinId: {}", coinId, e);
            throw new ExternalApiException("An unexpected error occurred while fetching coin data", e);
        }
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
}
