package ru.kslacker.fintech.security.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole(T(ru.kslacker.fintech.dataaccess.enums.UserRole).ADMIN)")
public @interface IsAdmin {
}
