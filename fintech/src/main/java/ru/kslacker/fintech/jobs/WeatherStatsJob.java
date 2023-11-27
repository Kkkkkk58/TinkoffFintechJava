package ru.kslacker.fintech.jobs;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kslacker.fintech.dataaccess.entities.City;
import ru.kslacker.fintech.dataaccess.repositories.api.CityRepository;
import ru.kslacker.fintech.dto.WeatherDto;
import ru.kslacker.fintech.service.api.CityWeatherService;

@Component
public class WeatherStatsJob {

    private final CityWeatherService cityWeatherService;
    private final WeatherStatsProducer weatherStatsProducer;
    private final List<UUID> cityIds;

    public WeatherStatsJob(
            CityWeatherService cityWeatherService,
            WeatherStatsProducer weatherStatsProducer,
            @Value("${weather.stats.cities:Moscow,Saint-Petersburg,Penza,Irkutsk,London}") String[] cities,
            CityRepository cityRepository) {

        this.cityWeatherService = cityWeatherService;
        this.weatherStatsProducer = weatherStatsProducer;
        this.cityIds = Arrays.stream(cities)
                .map(c -> cityRepository.findByName(c).orElse(null))
                .filter(Objects::nonNull)
                .map(City::getId)
                .toList();
    }

    @Scheduled(cron = "${weather.stats.cron:0 0 * * * *}")
    public void computeStats() {
        for (UUID id : cityIds) {
            List<WeatherDto> weather = cityWeatherService.getWeather(id, LocalDate.now());
            if (!weather.isEmpty()) {
                weatherStatsProducer.sendMessage(weather.get(0));
            }
        }
    }
}
