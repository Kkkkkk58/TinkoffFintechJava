package ru.kslacker.fintech.service.api;

import ru.kslacker.fintech.dto.FullWeatherInfoDto;

public interface CurrentWeatherSaver {
    void saveCurrentWeather(FullWeatherInfoDto fullWeatherInfoDto);
}
