package ru.kslacker.fintech.exceptions;

public class PersistenceException extends WeatherApplicationException {
    public PersistenceException(String message) {
        super("Persistence error occurred: " + message);
    }
}
