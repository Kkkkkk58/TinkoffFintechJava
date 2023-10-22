package ru.kslacker.fintech.mapping;

import ru.kslacker.fintech.dataaccess.entities.WeatherTypeInfo;
import ru.kslacker.fintech.dto.WeatherTypeInfoDto;

public class WeatherTypeInfoMapper {
    public static WeatherTypeInfoDto asDto(WeatherTypeInfo weatherTypeInfo) {
        return weatherTypeInfo == null
                ? null
                : new WeatherTypeInfoDto(weatherTypeInfo.getType(), weatherTypeInfo.getIconUrl());
    }
}
