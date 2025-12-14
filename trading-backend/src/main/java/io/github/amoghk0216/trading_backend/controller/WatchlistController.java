package io.github.amoghk0216.trading_backend.controller;

import io.github.amoghk0216.trading_backend.dto.WatchlistItemDto;
import io.github.amoghk0216.trading_backend.model.User;
import io.github.amoghk0216.trading_backend.service.WatchlistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/watchlist")
public class WatchlistController {
    private static final Logger logger = LoggerFactory.getLogger(WatchlistController.class);
    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService){
        this.watchlistService = watchlistService;
    }

    @PostMapping("/add/{coinId}")
    public ResponseEntity<WatchlistItemDto> addToWatchlist(@AuthenticationPrincipal User user, @PathVariable String coinId){
        logger.debug("Received add to watchlist request for coin: {}", coinId);
        WatchlistItemDto addedItem = watchlistService.addToWatchlist(user.getId(), coinId);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedItem);
    }

    @GetMapping
    public ResponseEntity<List<WatchlistItemDto>> getWatchlist(@AuthenticationPrincipal User user){
        logger.debug("Received request to get watchlist");
        List<WatchlistItemDto> watchlist = watchlistService.fetchWatchList(user.getId());
        return ResponseEntity.ok(watchlist);
    }

    @DeleteMapping("/delete/{coinId}")
    public ResponseEntity<?> deleteFromWatchlist(@AuthenticationPrincipal User user, @PathVariable String coinId){
        logger.debug("Received request to remove: {} from watchlist", coinId);
        watchlistService.removeFromWatchlist(user.getId(), coinId);
        return ResponseEntity.ok(String.format("Successfully removed coin: %s from watchlist", coinId));
    }


    @GetMapping("/check/{coinId}")
    public ResponseEntity<Map<String, Boolean>> inWatchlist(@AuthenticationPrincipal User user, @PathVariable String coinId){
        logger.debug("Received request to check: {} in watchlist", coinId);
        boolean inWatchlist = watchlistService.isInWatchlist(user.getId(), coinId);
        return ResponseEntity.ok(Map.of("inWatchlist", inWatchlist));
    }
}
