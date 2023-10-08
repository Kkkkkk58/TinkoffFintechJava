package ru.kslacker.fintech.exceptions;

import org.springframework.http.HttpStatus;

public class NoRemoteServiceResponseException extends RemoteWeatherClientException {
    public NoRemoteServiceResponseException() {
        super("Service is currently unavailable", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
