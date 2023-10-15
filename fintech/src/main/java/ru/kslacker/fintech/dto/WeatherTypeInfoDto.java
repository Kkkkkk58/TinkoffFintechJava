package ru.kslacker.fintech.dto;

import ru.kslacker.fintech.dataaccess.enums.WeatherType;

public record WeatherTypeInfoDto(WeatherType type, String iconUrl) {
}
