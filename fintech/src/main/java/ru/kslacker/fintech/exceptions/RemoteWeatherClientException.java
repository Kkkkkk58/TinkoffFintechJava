package ru.kslacker.fintech.exceptions;

import org.springframework.http.HttpStatus;

public abstract class RemoteWeatherClientException extends WeatherApplicationException {
    private final HttpStatus status;

    protected RemoteWeatherClientException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
