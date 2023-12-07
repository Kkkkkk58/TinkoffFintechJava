package ru.kslacker.fintech.cache.inmemory.properties;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "cache.course")
@Getter
@Setter
public class InMemoryWeatherCacheRepositoryProperties {
    @Min(0)
    private int size = 14;
}
