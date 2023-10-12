package ru.kslacker.fintech.service.api;

import ru.kslacker.fintech.dto.FullWeatherInfoDto;

public interface CurrentWeatherService {
    FullWeatherInfoDto getCurrentWeather(String location);
}
