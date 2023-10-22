package ru.kslacker.fintech.mapping;

import ru.kslacker.fintech.dto.WeatherDto;
import ru.kslacker.fintech.dataaccess.entities.Weather;

public class WeatherMapper {
    public static WeatherDto asDto(Weather weather) {
        return WeatherDto.builder()
                .cityId(weather.getCityId())
                .cityName(weather.getCityName())
                .type(WeatherTypeInfoMapper.asDto(weather.getType()))
                .temperatureValue(weather.getTemperatureValue())
                .dateTime(weather.getDateTime())
                .build();
    }
}
