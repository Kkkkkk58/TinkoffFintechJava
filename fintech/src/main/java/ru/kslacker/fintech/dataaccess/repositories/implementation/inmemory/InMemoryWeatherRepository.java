package ru.kslacker.fintech.dataaccess.repositories.implementation.inmemory;

import org.springframework.stereotype.Repository;
import ru.kslacker.fintech.dataaccess.entities.Weather;
import ru.kslacker.fintech.dataaccess.repositories.api.WeatherRepository;
import ru.kslacker.fintech.exceptions.CityAlreadyExistsException;
import ru.kslacker.fintech.exceptions.CityNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class InMemoryWeatherRepository implements WeatherRepository {

    private final Map<UUID, List<Weather>> cityWeather;

    public InMemoryWeatherRepository() {
        this.cityWeather = new HashMap<>();
    }

    @Override
    public List<Weather> findAll() {
        return cityWeather.values().stream().flatMap(Collection::stream).toList();
    }

    @Override
    public List<Weather> getByCityIdAndDateTimeBetween(UUID cityId, LocalDateTime from, LocalDateTime to) {
        if (!existsByCity(cityId)) {
            throw new CityNotFoundException(cityId);
        }

        return cityWeather.get(cityId).stream().filter(w ->
                (from.isBefore(w.getDateTime()) || from.isEqual(w.getDateTime())) && to.isAfter(w.getDateTime())).toList();
    }

    @Override
    public void createForCity(UUID cityId, List<Weather> weather) {
        if (existsByCity(cityId)) {
            throw new CityAlreadyExistsException(cityId);
        }
        cityWeather.put(cityId, new ArrayList<>(weather));
    }

    @Override
    public void updateWeather(Weather weather) {
        if (!existsByCity(weather.getCityId())) {
            throw new CityNotFoundException(weather.getCityId());
        }

        cityWeather.get(weather.getCityId()).removeIf(w -> w.getDateTime().isEqual(weather.getDateTime()));
        cityWeather.get(weather.getCityId()).add(weather);
    }

    @Override
    public void deleteByCityId(UUID cityId) {
        if (!existsByCity(cityId)) {
            throw new CityNotFoundException(cityId);
        }

        cityWeather.remove(cityId);
    }

    private boolean existsByCity(UUID cityId) {
        return cityWeather.containsKey(cityId);
    }
}
