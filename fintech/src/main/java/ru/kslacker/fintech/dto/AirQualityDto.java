package ru.kslacker.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record AirQualityDto(
        double co,
        double no2,
        double o3,
        int so2,
        @JsonProperty("pm2_5")
        double pm25,
        int pm10,
        @JsonProperty("us-epa-index")
        int usEpaIndex,
        @JsonProperty("gb-defra-index")
        int gbDefraIndex
) {
}
