package ru.kslacker.fintech.config;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "weather.remote")
@Getter
@Setter
public class RemoteWeatherServiceProperties {
    @NotEmpty
    private String url = "http://api.weatherapi.com/v1";

    @NotEmpty
    private String apiKey;
}
