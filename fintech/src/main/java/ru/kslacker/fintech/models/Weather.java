package ru.kslacker.fintech.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Weather {
    private UUID regionId;
    private String regionName;
    private int temperatureValue;
    private LocalDateTime dateTime;

    public Weather(UUID regionId, String regionName, int temperatureValue, LocalDateTime dateTime) {
        this.regionId = regionId;
        this.regionName = regionName;
        this.temperatureValue = temperatureValue;
        this.dateTime = dateTime;
    }

    public UUID getRegionId() {
        return regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public int getTemperatureValue() {
        return temperatureValue;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setRegionId(UUID regionId) {
        this.regionId = regionId;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public void setTemperatureValue(int temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
