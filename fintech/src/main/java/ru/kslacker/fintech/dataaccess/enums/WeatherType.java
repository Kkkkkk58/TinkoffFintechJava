package ru.kslacker.fintech.dataaccess.enums;

import lombok.Getter;

@Getter
public enum WeatherType {
    CLEAR("Clear"),
    RAIN("Rain"),
    CLOUDS("Clouds"),
    THUNDERSTORM("Thunderstorm"),
    DRIZZLE("Drizzle"),
    SNOW("Snow"),
    FOG("Fog");

    private final String name;

    WeatherType(String name) {
        this.name = name;
    }

    public static WeatherType fromString(String value) {
        for (WeatherType type : WeatherType.values()) {
            if (type.getName().equalsIgnoreCase(value)) {
                return type;
            }
        }

        return null;
    }
}
