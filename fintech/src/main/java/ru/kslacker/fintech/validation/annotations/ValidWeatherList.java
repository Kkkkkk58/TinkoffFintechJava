package ru.kslacker.fintech.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.kslacker.fintech.validation.validators.WeatherListValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = WeatherListValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidWeatherList {

    String message() default "Weather list must not have intersections by datetime";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
