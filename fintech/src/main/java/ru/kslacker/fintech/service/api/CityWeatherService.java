package ru.kslacker.fintech.service.api;

import ru.kslacker.fintech.dto.CreateCityDto;
import ru.kslacker.fintech.dto.CreateWeatherDto;
import ru.kslacker.fintech.dto.WeatherDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CityWeatherService {
    List<WeatherDto> getWeather(UUID cityId, LocalDate date);

    void createCity(UUID cityId, CreateCityDto createCityDto);

    void updateWeather(UUID cityId, CreateWeatherDto createWeatherDto);

    void deleteCity(UUID cityId);
}
