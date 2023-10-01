package ru.kslacker.fintech.dto;

import java.time.LocalDateTime;

public record CreateWeatherDto(double temperatureValue, LocalDateTime dateTime) {

}
