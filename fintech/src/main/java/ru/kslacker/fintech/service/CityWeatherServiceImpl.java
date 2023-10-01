package ru.kslacker.fintech.service;

import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Service;
import ru.kslacker.fintech.dataaccess.api.CityRepository;
import ru.kslacker.fintech.dataaccess.api.WeatherRepository;
import ru.kslacker.fintech.dto.CreateCityDto;
import ru.kslacker.fintech.dto.CreateWeatherDto;
import ru.kslacker.fintech.dto.WeatherDto;
import ru.kslacker.fintech.mapping.WeatherMapper;
import ru.kslacker.fintech.models.City;
import ru.kslacker.fintech.models.Weather;
import ru.kslacker.fintech.service.api.CityWeatherService;
import ru.kslacker.fintech.validation.service.api.ValidationService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@ExtensionMethod({WeatherMapper.class})
public class CityWeatherServiceImpl implements CityWeatherService {
    private final WeatherRepository weatherRepository;
    private final CityRepository cityRepository;
    private final ValidationService validator;

    public CityWeatherServiceImpl(WeatherRepository weatherRepository, CityRepository cityRepository, ValidationService validator) {
        this.weatherRepository = weatherRepository;
        this.cityRepository = cityRepository;
        this.validator = validator;
    }

    @Override
    public List<WeatherDto> getWeather(UUID cityId, LocalDate date) {
        return weatherRepository
                .getByCityAndDate(cityId, date)
                .stream()
                .map(w -> w.asDto())
                .toList();
    }

    @Override
    public void createCity(UUID cityId, CreateCityDto createCityDto) {
        validator.validate(createCityDto);
        City city = cityRepository.createCity(new City(cityId, createCityDto.name()));
        List<Weather> weather = createWeatherList(city, createCityDto.weather());
        weatherRepository.createForCity(cityId, weather);
    }

    @Override
    public void updateWeather(UUID cityId, CreateWeatherDto createWeatherDto) {
        City city = cityRepository.getById(cityId);
        Weather weather = createWeather(cityId, city.getName(), createWeatherDto);
        weatherRepository.updateWeather(weather);
    }

    @Override
    public void deleteCity(UUID cityId) {
        cityRepository.deleteById(cityId);
        weatherRepository.deleteByCity(cityId);
    }

    private List<Weather> createWeatherList(City city, List<CreateWeatherDto> weather) {
        return weather.stream().map(w -> createWeather(city.getId(), city.getName(), w)).toList();
    }

    private static Weather createWeather(UUID cityId, String cityName, CreateWeatherDto w) {
        return new Weather(cityId, cityName, w.temperatureValue(), w.dateTime());
    }
}
