package io.github.amoghk0216.trading_backend.controller;

import io.github.amoghk0216.trading_backend.dto.CoinResponseDto;
import io.github.amoghk0216.trading_backend.service.CoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}