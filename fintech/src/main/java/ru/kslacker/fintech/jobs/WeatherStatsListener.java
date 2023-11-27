package ru.kslacker.fintech.jobs;

import ru.kslacker.fintech.dto.WeatherDto;

public interface WeatherStatsListener {
    void receiveMessage(WeatherDto weatherDto);
}
