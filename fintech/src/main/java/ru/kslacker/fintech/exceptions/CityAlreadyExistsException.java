package ru.kslacker.fintech.exceptions;

import java.util.UUID;

public class CityAlreadyExistsException extends WeatherApplicationException {
    public CityAlreadyExistsException(UUID cityId) {
        super("City with id " + cityId + " already existsById");
    }
}
