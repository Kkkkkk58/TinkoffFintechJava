package ru.kslacker.fintech.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.kslacker.fintech.validation.validators.PasswordValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Password {

    String message() default "Password format is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
