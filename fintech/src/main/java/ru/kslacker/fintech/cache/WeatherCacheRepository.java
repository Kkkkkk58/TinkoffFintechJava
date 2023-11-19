package ru.kslacker.fintech.cache;

import java.util.List;

import ru.kslacker.fintech.dto.WeatherDto;

public interface WeatherCacheRepository {
    List<WeatherDto> get(WeatherRequestCacheKey key);
    void put(WeatherRequestCacheKey key, List<WeatherDto> value);
    void invalidate(WeatherRequestCacheKey key);
}
