package ru.kslacker.fintech.exceptions;

public class WeatherApplicationException extends RuntimeException {
    public WeatherApplicationException(String message) {
        super(message);
    }
}
