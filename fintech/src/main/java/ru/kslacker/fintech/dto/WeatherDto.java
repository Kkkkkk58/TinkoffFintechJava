package ru.kslacker.fintech.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record WeatherDto(UUID cityId, String cityName, WeatherTypeInfoDto type, double temperatureValue, LocalDateTime dateTime) {

}