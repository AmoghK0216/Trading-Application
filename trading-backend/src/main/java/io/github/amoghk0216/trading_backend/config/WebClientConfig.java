package io.github.amoghk0216.trading_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${coingecko.api.baseurl}")
    private String coinGeckoBaseUrl;

    @Value("${coin.gecko.api.key}")
    private String coinGeckoApiKey;

    @Bean
    public WebClient coinGeckoWebClient(){
        return WebClient.builder()
                .baseUrl("https://api.coingecko.com/api/v3/")
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
