package ru.kslacker.fintech.dto;

import lombok.Builder;

@Builder
public record FullWeatherInfoDto(LocationDto location, CurrentWeatherDto current) {
}
