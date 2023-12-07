package ru.kslacker.fintech.mapping;

import ru.kslacker.fintech.dataaccess.entities.User;
import ru.kslacker.fintech.dto.UserDto;

public class UserMapper {
    public static UserDto asDto(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .build();
    }
}
