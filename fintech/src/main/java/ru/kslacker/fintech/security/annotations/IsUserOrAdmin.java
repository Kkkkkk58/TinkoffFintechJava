package ru.kslacker.fintech.security.annotations;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("""
hasAnyRole(T(ru.kslacker.fintech.dataaccess.enums.UserRole).ADMIN, T(ru.kslacker.fintech.dataaccess.enums.UserRole).USER)
""")
public @interface IsUserOrAdmin {
}
