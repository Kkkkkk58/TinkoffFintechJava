package ru.kslacker.fintech.models.user;

import jakarta.validation.Valid;

public record UserModel(
        @Valid Credentials credentials
) {
}
