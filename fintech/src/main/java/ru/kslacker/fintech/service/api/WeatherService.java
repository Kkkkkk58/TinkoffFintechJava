package ru.kslacker.fintech.service.api;

import ru.kslacker.fintech.models.Weather;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface WeatherService {
    Map<UUID, Double> getAverageByRegions();

    List<UUID> getRegionsWithTemperatureAbove(int temperature);

    Map<UUID, List<Integer>> getMapWithId();

    Map<Integer, List<Weather>> getMapWithTemperature();
}
