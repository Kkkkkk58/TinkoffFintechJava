package ru.kslacker.fintech.jobs.kafka;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.kslacker.fintech.dto.WeatherDto;
import ru.kslacker.fintech.jobs.WeatherStatsListener;

@Component
@Slf4j
public class KafkaWeatherStatsListener implements WeatherStatsListener {
    @Value("${weather.stats.days:30}")
    private int rollingWindow;

    private final Map<UUID, Deque<Double>> data;

    public KafkaWeatherStatsListener() {
        this.data = new HashMap<>();
    }

    @Override
    @KafkaListener(
            topics = "${weather.stats.kafka.topic:weather.stats}",
            groupId = "${weather.stats.kafka.groupId:weather.stats.group}",
            properties = {"spring.json.value.default.type=ru.kslacker.fintech.dto.WeatherDto"}
    )
    public void receiveMessage(WeatherDto weatherDto) {
        double rollingMean = getRollingMean(weatherDto.cityId(), weatherDto.temperatureValue());
        log.info("Rolling mean is {} for city {}, days: {}", rollingMean, weatherDto.cityName(), rollingWindow);
    }

    private double getRollingMean(UUID cityId, double temperatureValue) {
        data.putIfAbsent(cityId, new ArrayDeque<>());
        Deque<Double> temperatureValues = data.get(cityId);
        if (temperatureValues.size() == rollingWindow) {
            temperatureValues.removeFirst();
        }
        temperatureValues.addLast(temperatureValue);

        return temperatureValues.stream()
                .mapToDouble(a -> a)
                .average()
                .orElse(0.0);
    }
}
