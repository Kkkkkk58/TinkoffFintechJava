package ru.kslacker.fintech.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kslacker.fintech.dataaccess.api.WeatherRepository;
import ru.kslacker.fintech.models.Weather;

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
        UUID region1 = UUID.randomUUID();
        UUID region2 = UUID.randomUUID();
        List<Weather> weather = List.of(
                new Weather(region1, "1", 1.0, LocalDateTime.now()),
                new Weather(region2, "2", -2.0, LocalDateTime.now()),
                new Weather(region2, "2", -4.0, LocalDateTime.now())
        );
        when(weatherRepository.findAll()).thenReturn(weather);

        Map<UUID, Double> result = service.getAverageByCities();

        assertAll(
                () -> assertTrue(result.containsKey(region1)),
                () -> assertEquals(1, result.get(region1)),
                () -> assertTrue(result.containsKey(region2)),
                () -> assertEquals(-3, result.get(region2))
        );
    }

    @Test
    void getRegionsWithTemperatureAbove_isCorrect() {
        UUID region1 = UUID.randomUUID();
        UUID region2 = UUID.randomUUID();
        List<Weather> weather = List.of(
                new Weather(region1, "1", 1.0, LocalDateTime.now()),
                new Weather(region2, "2", -2.0, LocalDateTime.now()),
                new Weather(region2, "2", -4.0, LocalDateTime.now())
        );
        when(weatherRepository.findAll()).thenReturn(weather);

        List<UUID> actual = service.getCitiesWithTemperatureAbove(0.0);

        List<UUID> expected = List.of(region1);
        assertEquals(expected, actual);
    }

    @Test
    void getMapWithId_isCorrect() {
        UUID region1 = UUID.randomUUID();
        UUID region2 = UUID.randomUUID();
        List<Weather> weather = List.of(
                new Weather(region1, "1", 1, LocalDateTime.now()),
                new Weather(region2, "2", -2, LocalDateTime.now()),
                new Weather(region2, "2", -4, LocalDateTime.now())
        );
        when(weatherRepository.findAll()).thenReturn(weather);

        Map<UUID, List<Double>> actual = service.getMapWithId();

        Map<UUID, List<Double>> expected = Map.of(
                region1, List.of(1.0),
                region2, List.of(-2.0, -4.0)
        );
        assertEquals(expected, actual);
    }

    @Test
    void getMapWithTemperature_isCorrect() {
        UUID region1 = UUID.randomUUID();
        UUID region2 = UUID.randomUUID();
        Weather weather1 = new Weather(region1, "1", 1.0, LocalDateTime.now());
        Weather weather2 = new Weather(region2, "2", 1.0, LocalDateTime.now());
        Weather weather3 = new Weather(region2, "2", -1.0, LocalDateTime.now());
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
