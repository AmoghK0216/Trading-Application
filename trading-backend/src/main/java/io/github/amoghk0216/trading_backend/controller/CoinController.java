package io.github.amoghk0216.trading_backend.controller;

import io.github.amoghk0216.trading_backend.dto.CoinResponseDto;
import io.github.amoghk0216.trading_backend.dto.CoinSearchResultDto;
import io.github.amoghk0216.trading_backend.service.CoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coins")
public class CoinController {

    private static final Logger logger = LoggerFactory.getLogger(CoinController.class);
    private final CoinService coinService;

    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @GetMapping("/{coinId}")
    public ResponseEntity<CoinResponseDto> getCoinData(@PathVariable String coinId) {
        logger.debug("Received request to fetch coin data for coinId: {}", coinId);
        CoinResponseDto coinData = coinService.getCoinById(coinId);
        logger.debug("Successfully returned coin data for coinId: {}", coinId);
        return ResponseEntity.ok(coinData);
    }

    @GetMapping("/search/{query}")
    public ResponseEntity<List<CoinSearchResultDto>> searchCoins(@PathVariable String query){
        logger.debug("Receive search request for query: {}", query);
        List<CoinSearchResultDto> coins = coinService.searchCoins(query);
        logger.debug("Sucessfully fetched search results for query: {}", query);
        return ResponseEntity.ok(coins);
    }
}