package io.github.amoghk0216.trading_backend.repository;

import io.github.amoghk0216.trading_backend.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    //find by userId
    List<Watchlist> findByUserId(Long userId);

    //check if entry exists
    boolean existsByUserIdAndCoinId(Long userId, String coinId);

    //find by userId and coinId
    Optional<Watchlist> findByUserIdAndCoinId(Long userId, String coinId);

    //delete by userId and coinId
    void deleteByUserIdAndCoinId(Long userId, String coinId);


    long countByUserId(Long userId);
}
