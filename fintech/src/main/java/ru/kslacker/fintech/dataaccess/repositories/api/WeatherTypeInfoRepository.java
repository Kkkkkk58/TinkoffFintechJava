package ru.kslacker.fintech.dataaccess.repositories.api;

import ru.kslacker.fintech.dataaccess.entities.WeatherTypeInfo;
import ru.kslacker.fintech.dataaccess.enums.WeatherType;

public interface WeatherTypeInfoRepository {
    WeatherTypeInfo getByType(WeatherType type);
}
