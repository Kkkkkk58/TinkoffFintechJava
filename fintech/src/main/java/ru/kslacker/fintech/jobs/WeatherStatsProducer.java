package ru.kslacker.fintech.jobs;

import ru.kslacker.fintech.dto.WeatherDto;

public interface WeatherStatsProducer {
    void sendMessage(WeatherDto weatherDto);
}
