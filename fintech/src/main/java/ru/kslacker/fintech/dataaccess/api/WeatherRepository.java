package ru.kslacker.fintech.dataaccess.api;

import ru.kslacker.fintech.models.Weather;

import java.util.List;

public interface WeatherRepository {
    List<Weather> findAll();
}
