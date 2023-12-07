package ru.kslacker.fintech.exceptions;

public class UserBuilderException extends WeatherApplicationException {
    public UserBuilderException(String message) {
        super(message);
    }

    public static UserBuilderException missingCredentials() {
        return new UserBuilderException("Credentials for new user were not provided");
    }
}
