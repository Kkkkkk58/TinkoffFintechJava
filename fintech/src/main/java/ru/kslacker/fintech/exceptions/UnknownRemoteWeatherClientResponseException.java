package ru.kslacker.fintech.exceptions;

import org.springframework.http.HttpStatus;

public class UnknownRemoteWeatherClientResponseException extends RemoteWeatherClientException {
    public UnknownRemoteWeatherClientResponseException() {
        super("Unexpected error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
