package ru.kslacker.fintech.exceptions;

import java.util.UUID;

public class CityNotFoundException extends WeatherApplicationException {
    public CityNotFoundException(UUID cityId) {
        super("City with id " + cityId + " not found");
    }
}
