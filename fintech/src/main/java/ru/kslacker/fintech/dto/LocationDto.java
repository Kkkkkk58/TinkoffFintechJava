package ru.kslacker.fintech.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.ZoneId;

public record LocationDto(
        String name,
        String region,
        String country,
        @JsonProperty("lat")
        double latitude,
        @JsonProperty("lon")
        double longitude,
        @JsonProperty("tz_id")
        ZoneId timeZoneId,
        @JsonProperty("localtime_epoch")
        int localtimeEpoch,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime localtime) {
}
