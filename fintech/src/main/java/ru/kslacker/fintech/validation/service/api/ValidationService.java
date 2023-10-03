package ru.kslacker.fintech.validation.service.api;

public interface ValidationService {

    <T> void validate(T t);
}
