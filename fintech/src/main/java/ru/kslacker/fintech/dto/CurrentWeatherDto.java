package ru.kslacker.fintech.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CurrentWeatherDto(
        @JsonProperty("last_updated_epoch")
        int lastUpdatedEpoch,
        @JsonProperty("last_updated")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime lastUpdated,
        @JsonProperty("temp_c")
        double tempCelsius,
        @JsonProperty("temp_f")
        double tempFahrenheit,
        @JsonProperty("is_day")
        int isDay,
        ConditionDto condition,
        @JsonProperty("wind_mph")
        double windMph,
        @JsonProperty("wind_kph")
        double windKph,
        @JsonProperty("wind_degree")
        int windDegree,
        @JsonProperty("wind_dir")
        String windDir,
        @JsonProperty("pressure_mb")
        int pressureMb,
        @JsonProperty("pressure_in")
        double pressureIn,
        @JsonProperty("precip_mm")
        int precipitationMm,
        @JsonProperty("precip_in")
        int precipitationIn,
        int humidity,
        int cloud,
        @JsonProperty("feelslike_c")
        int feelsLikeCelcius,
        @JsonProperty("feelslike_f")
        int feelsLikeFahrenheit,
        @JsonProperty("vis_km")
        int visKm,
        @JsonProperty("vis_miles")
        int visMiles,
        int uv,
        @JsonProperty("gust_mph")
        double gustMph,
        @JsonProperty("gust_kph")
        double gustKph,
        @JsonProperty("air_quality")
        AirQualityDto airQuality
) {
}
