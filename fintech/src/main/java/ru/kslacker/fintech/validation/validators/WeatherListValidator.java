package ru.kslacker.fintech.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.kslacker.fintech.dto.CreateWeatherDto;
import ru.kslacker.fintech.validation.annotations.ValidWeatherList;

import java.util.List;

public class WeatherListValidator implements ConstraintValidator<ValidWeatherList, List<CreateWeatherDto>> {

    @Override
    public boolean isValid(List<CreateWeatherDto> value, ConstraintValidatorContext context) {
        return value.stream().map(CreateWeatherDto::dateTime).distinct().count() == value.size();
    }
}
