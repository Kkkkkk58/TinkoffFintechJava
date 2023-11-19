package ru.kslacker.fintech.cache.inmemory;

import java.util.List;

import org.springframework.stereotype.Repository;
import ru.kslacker.fintech.cache.WeatherCacheRepository;
import ru.kslacker.fintech.cache.WeatherRequestCacheKey;
import ru.kslacker.fintech.cache.inmemory.properties.InMemoryWeatherCacheRepositoryProperties;
import ru.kslacker.fintech.dto.WeatherDto;

@Repository
public class InMemoryWeatherCacheRepository implements WeatherCacheRepository {

    private final LRUCache<WeatherRequestCacheKey, List<WeatherDto>> cache;

    public InMemoryWeatherCacheRepository(InMemoryWeatherCacheRepositoryProperties properties) {
        this.cache = new LRUCache<>(properties.getSize());
    }

    @Override
    public List<WeatherDto> get(WeatherRequestCacheKey key) {
        return cache.find(key);
    }

    @Override
    public void put(WeatherRequestCacheKey key, List<WeatherDto> value) {
        cache.put(key, value);
    }

    @Override
    public void invalidate(WeatherRequestCacheKey key) {
        cache.remove(key);
    }

}
