package ru.kslacker.fintech.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidRemoteServiceApiKeyException extends RemoteWeatherClientException {
    public InvalidRemoteServiceApiKeyException() {
        super("Service is unavailable", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
