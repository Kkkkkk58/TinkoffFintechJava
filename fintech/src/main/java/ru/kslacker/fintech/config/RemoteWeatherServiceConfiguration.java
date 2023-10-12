package ru.kslacker.fintech.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kslacker.fintech.middleware.RemoteWeatherServiceStatusHandler;

@Configuration
@EnableConfigurationProperties(RemoteWeatherServiceProperties.class)
public class RemoteWeatherServiceConfiguration {
    private final RemoteWeatherServiceProperties properties;

    public RemoteWeatherServiceConfiguration(RemoteWeatherServiceProperties properties) {
        this.properties = properties;
    }

    @Bean
    public WebClient remoteWeatherServiceWebClient(RemoteWeatherServiceStatusHandler statusHandler) {
        ExchangeFilterFunction errorResponseFilter = ExchangeFilterFunction
                .ofResponseProcessor(statusHandler::exchangeFilterResponseProcessor);

        return WebClient.builder()
                .filter(errorResponseFilter)
                .baseUrl(properties.getUrl())
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder
                .json()
                .modules(new JavaTimeModule())
                .build();
    }
}
