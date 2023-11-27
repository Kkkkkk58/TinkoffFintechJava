package ru.kslacker.fintech.models.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import ru.kslacker.fintech.validation.annotations.Password;

@Builder
public record Credentials(@NotBlank String username,
                          @Email String email,
                          @Password String password) {

}