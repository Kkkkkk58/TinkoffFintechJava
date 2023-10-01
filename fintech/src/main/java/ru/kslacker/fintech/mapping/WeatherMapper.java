package ru.kslacker.fintech.mapping;

import lombok.experimental.UtilityClass;
import ru.kslacker.fintech.dto.WeatherDto;
import ru.kslacker.fintech.models.Weather;

@UtilityClass
public class WeatherMapper {
    public static WeatherDto asDto(Weather weather) {
        return WeatherDto.builder()
                .cityId(weather.getCityId())
                .cityName(weather.getCityName())
                .temperatureValue(weather.getTemperatureValue())
                .dateTime(weather.getDateTime())
                .build();
    }
}
