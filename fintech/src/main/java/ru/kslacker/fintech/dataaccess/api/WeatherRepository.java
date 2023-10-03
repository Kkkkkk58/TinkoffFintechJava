package ru.kslacker.fintech.dataaccess.api;

import ru.kslacker.fintech.models.Weather;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface WeatherRepository {
    List<Weather> findAll();

    List<Weather> getByCityAndDate(UUID cityId, LocalDate date);

    void createForCity(UUID cityId, List<Weather> weather);

    void updateWeather(Weather weather);

    void deleteByCity(UUID cityId);
}
