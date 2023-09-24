package ru.kslacker.fintech.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kslacker.fintech.dataaccess.api.WeatherRepository;
import ru.kslacker.fintech.models.Weather;
import ru.kslacker.fintech.service.api.WeatherService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceImplTest {
    @Mock
    private WeatherRepository weatherRepository;
    private WeatherService service;

    @BeforeEach
    void init() {
        this.service = new WeatherServiceImpl(weatherRepository);
    }

    @Test
    void getAverageByRegions_isCorrect() {
        UUID region1 = UUID.randomUUID();
        UUID region2 = UUID.randomUUID();
        List<Weather> weather = List.of(
                new Weather(region1, "1", 1, LocalDateTime.now()),
                new Weather(region2, "2", -2, LocalDateTime.now()),
                new Weather(region2, "2", -4, LocalDateTime.now())
        );
        when(weatherRepository.findAll()).thenReturn(weather);

        Map<UUID, Double> result = service.getAverageByRegions();

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
                new Weather(region1, "1", 1, LocalDateTime.now()),
                new Weather(region2, "2", -2, LocalDateTime.now()),
                new Weather(region2, "2", -4, LocalDateTime.now())
        );
        when(weatherRepository.findAll()).thenReturn(weather);

        List<UUID> actual = service.getRegionsWithTemperatureAbove(0);

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

        Map<UUID, List<Integer>> actual = service.getMapWithId();

        Map<UUID, List<Integer>> expected = Map.of(
                region1, List.of(1),
                region2, List.of(-2, -4)
        );
        assertEquals(expected, actual);
    }

    @Test
    void getMapWithTemperature_isCorrect() {
        UUID region1 = UUID.randomUUID();
        UUID region2 = UUID.randomUUID();
        Weather weather1 = new Weather(region1, "1", 1, LocalDateTime.now());
        Weather weather2 = new Weather(region2, "2", 1, LocalDateTime.now());
        Weather weather3 = new Weather(region2, "2", -1, LocalDateTime.now());
        List<Weather> weather = List.of(weather1, weather2, weather3);
        when(weatherRepository.findAll()).thenReturn(weather);

        Map<Integer, List<Weather>> actual = service.getMapWithTemperature();

        Map<Integer, List<Weather>> expected = Map.of(
                1, List.of(weather1, weather2),
                -1, List.of(weather3)
        );
        assertEquals(expected, actual);
    }
}
