package ru.kslacker.fintech.middleware;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kslacker.fintech.exceptions.CityAlreadyExistsException;
import ru.kslacker.fintech.exceptions.CityNotFoundException;
import ru.kslacker.fintech.exceptions.RemoteServiceUnavailableException;
import ru.kslacker.fintech.exceptions.RemoteWeatherClientException;
import ru.kslacker.fintech.response.ErrorResponse;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(CityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleCityNotFoundException(CityNotFoundException exception) {
        return handleException(exception);
    }

    @ExceptionHandler(CityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCityAlreadyExistsException(CityAlreadyExistsException exception) {
        return handleException(exception);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {
        return handleException(exception);
    }

    @ExceptionHandler(RemoteWeatherClientException.class)
    public ResponseEntity<ErrorResponse> handleRemoteWeatherClientException(RemoteWeatherClientException exception) {
        return ResponseEntity.status(exception.getStatus()).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(RequestNotPermitted.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public void handleRequestNotPermitted() {
        handleException(new RemoteServiceUnavailableException());
    }

    private ErrorResponse handleException(Exception e) {
        return new ErrorResponse(e.getMessage());
    }
}