package ru.kslacker.fintech.service;

import ru.kslacker.fintech.dataaccess.api.WeatherRepository;
import ru.kslacker.fintech.models.Weather;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class WeatherStatisticsService {
    private final WeatherRepository weatherRepository;

    public WeatherStatisticsService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public Map<UUID, Double> getAverageByCities() {
        return weatherRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Weather::getCityId,
                        Collectors.averagingDouble(Weather::getTemperatureValue)));
    }

    public List<UUID> getCitiesWithTemperatureAbove(double temperature) {
        return weatherRepository.findAll()
                .stream()
                .filter(weather -> Double.compare(weather.getTemperatureValue(), temperature) > 0)
                .map(Weather::getCityId)
                .distinct()
                .toList();
    }

    public Map<UUID, List<Double>> getMapWithId() {
        return weatherRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Weather::getCityId,
                        Collectors.mapping(Weather::getTemperatureValue, Collectors.toList())));
    }

    public Map<Double, List<Weather>> getMapWithTemperature() {
        return weatherRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Weather::getTemperatureValue));
    }
}
