package ru.kslacker.fintech.exceptions;

import org.springframework.http.HttpStatus;

public class BadRemoteRequestException extends RemoteWeatherClientException {
    private BadRemoteRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public static BadRemoteRequestException LocationNotProvided() {
        return new BadRemoteRequestException("Location was not provided");
    }

    public static BadRemoteRequestException InvalidRequestUrl() {
        return new BadRemoteRequestException("Invalid request url");
    }

    public static BadRemoteRequestException InvalidBulkRequestBody() {
        return new BadRemoteRequestException("Invalid request body");
    }
}
