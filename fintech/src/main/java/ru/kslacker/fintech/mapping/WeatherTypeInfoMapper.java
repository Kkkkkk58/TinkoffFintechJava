package ru.kslacker.fintech.mapping;

import ru.kslacker.fintech.dataaccess.entities.WeatherTypeInfo;
import ru.kslacker.fintech.dto.WeatherTypeInfoDto;

public class WeatherTypeInfoMapper {
    public static WeatherTypeInfoDto asDto(WeatherTypeInfo weatherTypeInfo) {
        if (weatherTypeInfo == null) {
            return null;
        }
        return new WeatherTypeInfoDto(weatherTypeInfo.getType(), weatherTypeInfo.getIconUrl());
    }
}
