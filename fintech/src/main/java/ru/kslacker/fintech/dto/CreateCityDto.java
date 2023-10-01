package ru.kslacker.fintech.dto;

import ru.kslacker.fintech.validation.annotations.ValidWeatherList;

import java.util.List;

public record CreateCityDto(String name, @ValidWeatherList List<CreateWeatherDto> weather) {

}
