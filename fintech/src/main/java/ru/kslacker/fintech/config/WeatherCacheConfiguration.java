package ru.kslacker.fintech.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.kslacker.fintech.cache.inmemory.properties.InMemoryWeatherCacheRepositoryProperties;

@Configuration
@EnableConfigurationProperties(InMemoryWeatherCacheRepositoryProperties.class)
public class WeatherCacheConfiguration {
}
