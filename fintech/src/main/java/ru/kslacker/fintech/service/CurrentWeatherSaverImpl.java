package ru.kslacker.fintech.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.kslacker.fintech.dataaccess.entities.City;
import ru.kslacker.fintech.dataaccess.entities.Weather;
import ru.kslacker.fintech.dataaccess.entities.WeatherTypeInfo;
import ru.kslacker.fintech.dataaccess.enums.WeatherType;
import ru.kslacker.fintech.dataaccess.repositories.api.CityRepository;
import ru.kslacker.fintech.dataaccess.repositories.api.WeatherRepository;
import ru.kslacker.fintech.dataaccess.repositories.api.WeatherTypeInfoRepository;
import ru.kslacker.fintech.dto.CurrentWeatherDto;
import ru.kslacker.fintech.dto.FullWeatherInfoDto;
import ru.kslacker.fintech.exceptions.PersistenceException;
import ru.kslacker.fintech.service.api.CurrentWeatherSaver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Service
public class CurrentWeatherSaverImpl implements CurrentWeatherSaver {
    private final CityRepository cityRepository;
    private final WeatherRepository weatherRepository;
    private final WeatherTypeInfoRepository weatherTypeInfoRepository;
    private final JdbcTemplate jdbcTemplate;

    public CurrentWeatherSaverImpl(
            CityRepository cityRepository,
            WeatherRepository weatherRepository,
            WeatherTypeInfoRepository weatherTypeInfoRepository,
            JdbcTemplate jdbcTemplate) {
        this.cityRepository = cityRepository;
        this.weatherRepository = weatherRepository;
        this.weatherTypeInfoRepository = weatherTypeInfoRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveCurrentWeather(FullWeatherInfoDto fullWeatherInfoDto) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            try {
                connection.setAutoCommit(false);
                connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);

                String cityName = fullWeatherInfoDto.location().name();
                City city = cityRepository.findByName(cityName).orElseGet(() -> createCity(cityName));
                Weather weather = getWeatherFromCurrentWeather(city, fullWeatherInfoDto.current());
                weatherRepository.createForCity(city.getId(), List.of(weather));

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    private City createCity(String name) {
        City city = new City(UUID.randomUUID(), name);
        return cityRepository.save(city);
    }

    private Weather getWeatherFromCurrentWeather(City city, CurrentWeatherDto current) {
        return new Weather(city, getWeatherTypeInfo(current.condition().text()), current.tempCelsius(), current.lastUpdated());
    }

    private WeatherTypeInfo getWeatherTypeInfo(String conditionValue) {
        WeatherType type = WeatherType.fromString(conditionValue.toUpperCase());
        return (type == null) ? null : weatherTypeInfoRepository.getByType(type);
    }
}
