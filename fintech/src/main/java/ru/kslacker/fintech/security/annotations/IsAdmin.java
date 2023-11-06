package ru.kslacker.fintech.security.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole(T(ru.kslacker.fintech.dataaccess.enums.UserRole).ADMIN)")
public @interface IsAdmin {
}
