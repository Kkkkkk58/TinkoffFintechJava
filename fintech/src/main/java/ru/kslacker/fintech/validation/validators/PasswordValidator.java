package ru.kslacker.fintech.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.kslacker.fintech.validation.annotations.Password;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return true;
    }
}