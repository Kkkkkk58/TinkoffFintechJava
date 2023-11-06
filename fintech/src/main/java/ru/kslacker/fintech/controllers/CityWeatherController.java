package ru.kslacker.fintech.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.kslacker.fintech.dto.CreateCityDto;
import ru.kslacker.fintech.dto.CreateWeatherDto;
import ru.kslacker.fintech.dto.WeatherDto;
import ru.kslacker.fintech.security.annotations.IsAdmin;
import ru.kslacker.fintech.security.annotations.IsUserOrAdmin;
import ru.kslacker.fintech.service.api.CityWeatherService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/weather")
@PreAuthorize("isAuthenticated()")
@SecurityRequirement(name = "basicAuth")
public class CityWeatherController {

    private final CityWeatherService cityWeatherService;

    public CityWeatherController(CityWeatherService cityWeatherService) {
        this.cityWeatherService = cityWeatherService;
    }

    @GetMapping(value = "{cityId}", produces = "application/json")
    @IsUserOrAdmin
    public List<WeatherDto> getWeather(@PathVariable UUID cityId, @RequestParam LocalDate date) {
        return cityWeatherService.getWeather(cityId, date);
    }

    @PostMapping("{cityId}")
    @IsAdmin
    public void createCity(@PathVariable UUID cityId, @RequestBody CreateCityDto createCityDto) {
        cityWeatherService.createCity(cityId, createCityDto);
    }

    @PutMapping("{cityId}")
    @IsAdmin
    public void updateWeather(@PathVariable UUID cityId, @RequestBody CreateWeatherDto createWeatherDto) {
        cityWeatherService.updateWeather(cityId, createWeatherDto);
    }

    @DeleteMapping("{cityId}")
    @IsAdmin
    public void deleteCity(@PathVariable UUID cityId) {
        cityWeatherService.deleteCity(cityId);
    }
}
