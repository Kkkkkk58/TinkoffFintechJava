package ru.kslacker.fintech.controllers;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kslacker.fintech.dto.FullWeatherInfoDto;
import ru.kslacker.fintech.security.annotations.IsUserOrAdmin;
import ru.kslacker.fintech.service.api.CurrentWeatherService;

@RestController
@RequestMapping("api/weather/current")
@SecurityRequirement(name = "basicAuth")
public class CurrentWeatherController {
    private final CurrentWeatherService service;

    public CurrentWeatherController(CurrentWeatherService service) {
        this.service = service;
    }

    @GetMapping(produces = "application/json")
    @IsUserOrAdmin
    @RateLimiter(name = "remote-weather-service")
    public FullWeatherInfoDto getCurrentWeather(@RequestParam String location) {
        return service.getCurrentWeather(location);
    }
}
