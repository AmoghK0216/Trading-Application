package io.github.amoghk0216.trading_backend.controller;

import io.github.amoghk0216.trading_backend.dto.CoinResponseDto;
import io.github.amoghk0216.trading_backend.service.CoinService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coins")
public class CoinController {

    private final CoinService coinService;

    public CoinController(CoinService coinService) {
        this.coinService = coinService;
    }

    @GetMapping("/{coinId}")
    public ResponseEntity<CoinResponseDto> getCoinData(@PathVariable String coinId) {
        CoinResponseDto coinData = coinService.getCoinById(coinId);
        return ResponseEntity.ok(coinData);
    }
}