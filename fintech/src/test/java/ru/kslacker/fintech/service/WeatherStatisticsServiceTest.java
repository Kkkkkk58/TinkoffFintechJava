package ru.kslacker.fintech.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kslacker.fintech.dataaccess.entities.City;
import ru.kslacker.fintech.dataaccess.repositories.api.WeatherRepository;
import ru.kslacker.fintech.dataaccess.entities.Weather;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherStatisticsServiceTest {
    @Mock
    private WeatherRepository weatherRepository;
    private WeatherStatisticsService service;

    @BeforeEach
    void init() {
        this.service = new WeatherStatisticsService(weatherRepository);
    }

    @Test
    void getAverageByRegions_isCorrect() {
        City city1 = new City(UUID.randomUUID(), "1");
        City city2 = new City(UUID.randomUUID(), "2");
        List<Weather> weather = List.of(
                new Weather(city1, null, 1.0, LocalDateTime.now()),
                new Weather(city2, null, -2.0, LocalDateTime.now()),
                new Weather(city2, null, -4.0, LocalDateTime.now())
        );
        when(weatherRepository.findAll()).thenReturn(weather);

        Map<UUID, Double> result = service.getAverageByCities();

        assertAll(
                () -> assertTrue(result.containsKey(city1.getId())),
                () -> assertEquals(1, result.get(city1.getId())),
                () -> assertTrue(result.containsKey(city2.getId())),
                () -> assertEquals(-3, result.get(city2.getId()))
        );
    }

    @Test
    void getRegionsWithTemperatureAbove_isCorrect() {
        City city1 = new City(UUID.randomUUID(), "1");
        City city2 = new City(UUID.randomUUID(), "2");
        List<Weather> weather = List.of(
                new Weather(city1, null, 1.0, LocalDateTime.now()),
                new Weather(city2, null, -2.0, LocalDateTime.now()),
                new Weather(city2, null, -4.0, LocalDateTime.now())
        );
        when(weatherRepository.findAll()).thenReturn(weather);

        List<UUID> actual = service.getCitiesWithTemperatureAbove(0.0);

        List<UUID> expected = List.of(city1.getId());
        assertEquals(expected, actual);
    }

    @Test
    void getMapWithId_isCorrect() {
        City city1 = new City(UUID.randomUUID(), "1");
        City city2 = new City(UUID.randomUUID(), "2");
        List<Weather> weather = List.of(
                new Weather(city1, null, 1, LocalDateTime.now()),
                new Weather(city2, null, -2, LocalDateTime.now()),
                new Weather(city2, null, -4, LocalDateTime.now())
        );
        when(weatherRepository.findAll()).thenReturn(weather);

        Map<UUID, List<Double>> actual = service.getMapWithId();

        Map<UUID, List<Double>> expected = Map.of(
                city1.getId(), List.of(1.0),
                city2.getId(), List.of(-2.0, -4.0)
        );
        assertEquals(expected, actual);
    }

    @Test
    void getMapWithTemperature_isCorrect() {
        City city1 = new City(UUID.randomUUID(), "1");
        City city2 = new City(UUID.randomUUID(), "2");
        Weather weather1 = new Weather(city1, null, 1.0, LocalDateTime.now());
        Weather weather2 = new Weather(city1, null, 1.0, LocalDateTime.now());
        Weather weather3 = new Weather(city1, null, -1.0, LocalDateTime.now());
        List<Weather> weather = List.of(weather1, weather2, weather3);
        when(weatherRepository.findAll()).thenReturn(weather);

        Map<Double, List<Weather>> actual = service.getMapWithTemperature();

        Map<Double, List<Weather>> expected = Map.of(
                1.0, List.of(weather1, weather2),
                -1.0, List.of(weather3)
        );
        assertEquals(expected, actual);
    }
}
