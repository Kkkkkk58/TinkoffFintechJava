package ru.kslacker.fintech.dataaccess.repositories.api;

import ru.kslacker.fintech.dataaccess.entities.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    User save(User user);
}
