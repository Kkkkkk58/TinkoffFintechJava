package ru.kslacker.fintech.service.api;

import ru.kslacker.fintech.dto.UserDto;
import ru.kslacker.fintech.models.user.Credentials;

public interface UserService {
    UserDto create(Credentials credentials);
}
