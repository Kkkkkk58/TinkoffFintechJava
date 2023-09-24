package ru.kslacker.fintech.service;

import ru.kslacker.fintech.dataaccess.api.WeatherRepository;
import ru.kslacker.fintech.models.Weather;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class WeatherServiceImpl {
    private final WeatherRepository weatherRepository;

    public WeatherServiceImpl(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public Map<UUID, Double> getAverageByRegions() {
        return weatherRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Weather::getRegionId,
                        Collectors.averagingInt(Weather::getTemperatureValue)));
    }

    public List<UUID> getRegionsWithTemperatureAbove(int temperature) {
        return weatherRepository.findAll()
                .stream()
                .filter(weather -> weather.getTemperatureValue() > temperature)
                .map(Weather::getRegionId)
                .distinct()
                .toList();
    }

    public Map<UUID, List<Integer>> getMapWithId() {
        return weatherRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Weather::getRegionId,
                        Collectors.mapping(Weather::getTemperatureValue, Collectors.toList())));
    }

    public Map<Integer, List<Weather>> getMapWithTemperature() {
        return weatherRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Weather::getTemperatureValue));
    }
}
