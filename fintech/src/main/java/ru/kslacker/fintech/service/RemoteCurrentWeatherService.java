package ru.kslacker.fintech.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kslacker.fintech.config.RemoteWeatherServiceProperties;
import ru.kslacker.fintech.dto.FullWeatherInfoDto;
import ru.kslacker.fintech.exceptions.NoRemoteServiceResponseException;
import ru.kslacker.fintech.service.api.CurrentWeatherSaver;
import ru.kslacker.fintech.service.api.CurrentWeatherService;

@Service
public class RemoteCurrentWeatherService implements CurrentWeatherService {
    @Qualifier("remoteWeatherServiceWebClient")
    private final WebClient webClient;

    private final RemoteWeatherServiceProperties properties;

    private final CurrentWeatherSaver saver;

    public RemoteCurrentWeatherService(
            WebClient webClient,
            RemoteWeatherServiceProperties properties,
            CurrentWeatherSaver saver) {
        this.webClient = webClient;
        this.properties = properties;
        this.saver = saver;
    }

    @Override
    public FullWeatherInfoDto getCurrentWeather(String location) {
        FullWeatherInfoDto weatherInfoDto = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/current.json")
                        .queryParam("key", properties.getApiKey())
                        .queryParam("q", location)
                        .queryParam("aqi", "yes")
                        .build())
                .retrieve()
                .bodyToMono(FullWeatherInfoDto.class)
                .block();

        if (weatherInfoDto == null) {
            throw new NoRemoteServiceResponseException();
        }

        saver.saveCurrentWeather(weatherInfoDto);

        return weatherInfoDto;
    }
}
