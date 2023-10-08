package ru.kslacker.fintech.exceptions;

import org.springframework.http.HttpStatus;

public class RemoteServiceUnavailableException extends RemoteWeatherClientException {
    public RemoteServiceUnavailableException() {
        super("Service is unavailable", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
