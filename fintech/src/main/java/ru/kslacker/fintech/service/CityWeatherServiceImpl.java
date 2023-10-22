package ru.kslacker.fintech.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.kslacker.fintech.dataaccess.entities.WeatherTypeInfo;
import ru.kslacker.fintech.dataaccess.repositories.api.CityRepository;
import ru.kslacker.fintech.dataaccess.repositories.api.WeatherRepository;
import ru.kslacker.fintech.dataaccess.repositories.api.WeatherTypeInfoRepository;
import ru.kslacker.fintech.dto.CreateCityDto;
import ru.kslacker.fintech.dto.CreateWeatherDto;
import ru.kslacker.fintech.dto.WeatherDto;
import ru.kslacker.fintech.mapping.WeatherMapper;
import ru.kslacker.fintech.dataaccess.entities.City;
import ru.kslacker.fintech.dataaccess.entities.Weather;
import ru.kslacker.fintech.service.api.CityWeatherService;
import ru.kslacker.fintech.validation.service.api.ValidationService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CityWeatherServiceImpl implements CityWeatherService {
    private final WeatherRepository weatherRepository;
    private final CityRepository cityRepository;
    private final WeatherTypeInfoRepository weatherTypeInfoRepository;
    private final ValidationService validator;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public List<WeatherDto> getWeather(UUID cityId, LocalDate date) {
        return weatherRepository
                .getByCityIdAndDateTimeBetween(cityId, date.atStartOfDay(), date.plusDays(1).atStartOfDay())
                .stream()
                .map(WeatherMapper::asDto)
                .toList();
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void createCity(UUID cityId, CreateCityDto createCityDto) {
        validator.validate(createCityDto);
        City city = cityRepository.save(new City(cityId, createCityDto.name()));
        List<Weather> weather = createWeatherList(city, createCityDto.weather());
        weatherRepository.createForCity(cityId, weather);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateWeather(UUID cityId, CreateWeatherDto createWeatherDto) {
        City city = cityRepository.getById(cityId);
        Weather weather = createWeather(city, createWeatherDto);
        weatherRepository.updateWeather(weather);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteCity(UUID cityId) {
        cityRepository.deleteById(cityId);
        weatherRepository.deleteByCityId(cityId);
    }

    private List<Weather> createWeatherList(City city, List<CreateWeatherDto> weather) {
        return weather.stream().map(w -> createWeather(city, w)).toList();
    }

    private Weather createWeather(City city, CreateWeatherDto w) {
        WeatherTypeInfo weatherTypeInfo = weatherTypeInfoRepository.getByType(w.type());
        return new Weather(city, weatherTypeInfo, w.temperatureValue(), w.dateTime());
    }
}
