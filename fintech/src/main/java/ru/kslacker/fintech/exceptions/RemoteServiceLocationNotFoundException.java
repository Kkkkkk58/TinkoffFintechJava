package ru.kslacker.fintech.exceptions;

import org.springframework.http.HttpStatus;

public class RemoteServiceLocationNotFoundException extends RemoteWeatherClientException {
    public RemoteServiceLocationNotFoundException() {
        super("Location not found", HttpStatus.NOT_FOUND);
    }
}
