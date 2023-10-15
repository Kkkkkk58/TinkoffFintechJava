package ru.kslacker.fintech.dto;

import ru.kslacker.fintech.dataaccess.enums.WeatherType;

import java.time.LocalDateTime;

public record CreateWeatherDto(WeatherType type, double temperatureValue, LocalDateTime dateTime) {

}
