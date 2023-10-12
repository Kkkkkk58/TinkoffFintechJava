package ru.kslacker.fintech.exceptions;

import org.springframework.http.HttpStatus;

public class RemoteRequestBulkBodyTooBigException extends RemoteWeatherClientException {
    public RemoteRequestBulkBodyTooBigException() {
        super("Too many requests", HttpStatus.TOO_MANY_REQUESTS);
    }
}
