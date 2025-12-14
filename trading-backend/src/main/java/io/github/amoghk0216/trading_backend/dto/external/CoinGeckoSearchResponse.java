package io.github.amoghk0216.trading_backend.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CoinGeckoSearchResponse(
        List<CoinSearchItem> coins
){
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CoinSearchItem(
          String id,
          String name,
          String symbol,
          @JsonProperty("market_cap_rank")
          Integer marketCapRank,
          String thumb,
          String large
    ){}
}
