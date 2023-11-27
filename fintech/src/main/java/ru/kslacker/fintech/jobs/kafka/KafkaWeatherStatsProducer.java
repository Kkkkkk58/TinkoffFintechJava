package ru.kslacker.fintech.jobs.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.kslacker.fintech.dto.WeatherDto;
import ru.kslacker.fintech.jobs.WeatherStatsProducer;

@Component
public class KafkaWeatherStatsProducer implements WeatherStatsProducer {
    private final KafkaTemplate<String, WeatherDto> kafkaTemplate;

    @Value("${weather.stats.kafka.topic:weather.stats}")
    private String weatherStatsTopic;

    public KafkaWeatherStatsProducer(KafkaTemplate<String, WeatherDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(WeatherDto weatherDto) {
        kafkaTemplate.send(weatherStatsTopic, weatherDto.cityId().toString(), weatherDto);
    }
}
