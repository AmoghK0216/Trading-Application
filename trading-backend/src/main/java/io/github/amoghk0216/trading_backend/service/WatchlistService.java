package io.github.amoghk0216.trading_backend.service;

import io.github.amoghk0216.trading_backend.client.CoinGeckoClient;
import io.github.amoghk0216.trading_backend.dto.CoinResponseDto;
import io.github.amoghk0216.trading_backend.dto.WatchlistItemDto;
import io.github.amoghk0216.trading_backend.model.User;
import io.github.amoghk0216.trading_backend.model.Watchlist;
import io.github.amoghk0216.trading_backend.repository.UserRepository;
import io.github.amoghk0216.trading_backend.repository.WatchlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.webauthn.api.CredProtectAuthenticationExtensionsClientInput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WatchlistService {
    private static final Logger log = LoggerFactory.getLogger(WatchlistService.class);
    private static final int WATCHLIST_MAX_COUNT = 50;

    private final WatchlistRepository watchlistRepository;
    private final  UserRepository userRepository;
    private final CoinGeckoClient coinGeckoClient;

    public WatchlistService(WatchlistRepository watchlistRepository,
                            UserRepository userRepository,
                            CoinGeckoClient coinGeckoClient){
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.coinGeckoClient = coinGeckoClient;
    }

    @Transactional
    public WatchlistItemDto addToWatchlist(Long userId, String coinId){
        if(watchlistRepository.existsByUserIdAndCoinId(userId, coinId)){
            throw new RuntimeException("Coin Already in Watchlist");
        }

        if(watchlistRepository.countByUserId(userId) >= WATCHLIST_MAX_COUNT){
            throw new IllegalStateException("Watchlist full");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not Found"));

        CoinResponseDto coinResponseDto = coinGeckoClient.getCoinById(coinId);
        Watchlist watchlist = new Watchlist(
                user,
                coinResponseDto.id(),
                coinResponseDto.name(),
                coinResponseDto.symbol()
        );

        watchlistRepository.save(watchlist);

        return new WatchlistItemDto(
                watchlist.getId(),
                userId,
                coinId,
                coinResponseDto.name(),
                coinResponseDto.symbol(),
                coinResponseDto.priceUsd(),
                coinResponseDto.priceChangePercentage24h(),
                watchlist.getAddedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<WatchlistItemDto> fetchWatchList(Long userId){
        log.info("fetching watchlist");
        List<Watchlist> watchlistCoins = watchlistRepository.findByUserId(userId);
        if(watchlistCoins.isEmpty())
            return Collections.emptyList();

        List<String> coinIds = watchlistCoins.stream().map(
                Watchlist::getCoinId
        ).toList();

        List<CoinResponseDto> coinDetails;
        try{
            coinDetails = coinGeckoClient.getCoinsByIds(coinIds);
            log.info("Successfully fetched watchlist");
        }catch (Exception e){
            log.error("Failed to fetch coin prices: {}", e.getMessage());
            return watchlistCoins.stream()
                    .map(item -> new WatchlistItemDto(
                            item.getId(),
                            userId,
                            item.getCoinId(),
                            item.getCoinName(),
                            item.getCoinSymbol(),
                            null,
                            null,
                            item.getAddedAt()
                    )).toList();
        }

        Map<String, CoinResponseDto> coinMap = coinDetails.stream()
                .collect(Collectors.toMap(
                        CoinResponseDto::id,
                        coin -> coin
                ));

        return watchlistCoins.stream()
                .map(item -> {
                    CoinResponseDto coin = coinMap.get(item.getCoinId());
                    if(coin == null){
                        return new WatchlistItemDto(
                                item.getId(),
                                userId,
                                item.getCoinId(),
                                item.getCoinName(),
                                item.getCoinSymbol(),
                                null,
                                null,
                                item.getAddedAt()
                        );
                    }else{
                        return new WatchlistItemDto(
                                item.getId(),
                                userId,
                                item.getCoinId(),
                                item.getCoinName(),
                                item.getCoinSymbol(),
                                coin.priceUsd(),
                                coin.priceChangePercentage24h(),
                                item.getAddedAt()
                        );
                    }
                }).toList();
    }

    @Transactional
    public void removeFromWatchlist(Long userId, String coinId){
        log.info("remove coin: {} from watchlist", coinId);
        Watchlist watchlist = watchlistRepository.findByUserIdAndCoinId(userId, coinId)
                .orElseThrow(() -> new RuntimeException("Coin not in Watchlist"));

        watchlistRepository.delete(watchlist);
    }

    public boolean isInWatchlist(Long userId, String coinId){
        log.info("checking if coin: {} in watchlist", coinId);
        return watchlistRepository.existsByUserIdAndCoinId(userId, coinId);
    }
}
