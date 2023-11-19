package ru.kslacker.fintech.controllers.cache;

import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.kslacker.fintech.cache.CachingAspect;
import ru.kslacker.fintech.cache.inmemory.InMemoryWeatherCacheRepository;
import ru.kslacker.fintech.cache.inmemory.properties.InMemoryWeatherCacheRepositoryProperties;


@Configuration
@Import({
        AopAutoConfiguration.class,
        CachingAspect.class,
        InMemoryWeatherCacheRepository.class,
        InMemoryWeatherCacheRepositoryProperties.class
})
public class CityWeatherControllerCachingConfig {
}
