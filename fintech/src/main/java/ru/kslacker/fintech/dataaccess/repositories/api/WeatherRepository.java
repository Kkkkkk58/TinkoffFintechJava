package ru.kslacker.fintech.dataaccess.repositories.api;

import ru.kslacker.fintech.dataaccess.entities.Weather;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface WeatherRepository {
    List<Weather> findAll();

    List<Weather> getByCityIdAndDateTimeBetween(UUID cityId, LocalDateTime from, LocalDateTime to);

    void createForCity(UUID cityId, List<Weather> weather);

    void updateWeather(Weather weather);

    void deleteByCityId(UUID cityId);
}
