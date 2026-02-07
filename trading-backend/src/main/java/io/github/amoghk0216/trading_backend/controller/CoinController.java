package io.github.amoghk0216.trading_backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.amoghk0216.trading_backend.dto.CoinResponseDto;
import io.github.amoghk0216.trading_backend.dto.CoinSearchResultDto;
import io.github.amoghk0216.trading_backend.service.CoinService;

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

    @GetMapping("/bulk")
    public ResponseEntity<List<CoinResponseDto>> getBulkCoins(@RequestParam String ids) {
        logger.debug("Received bulk request for coin ids: {}", ids);
        List<String> coinIds = List.of(ids.split(","));
        List<CoinResponseDto> coins = coinService.getCoinsByIds(coinIds);
        logger.debug("Successfully returned {} coins", coins.size());
        return ResponseEntity.ok(coins);
    }
}