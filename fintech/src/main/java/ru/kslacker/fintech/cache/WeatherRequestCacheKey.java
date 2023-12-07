package ru.kslacker.fintech.cache;

import java.time.LocalDate;
import java.util.UUID;

public record WeatherRequestCacheKey(UUID cityId, LocalDate date) {
}
