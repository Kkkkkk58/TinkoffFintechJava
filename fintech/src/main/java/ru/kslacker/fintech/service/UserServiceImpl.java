package ru.kslacker.fintech.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kslacker.fintech.dataaccess.entities.User;
import ru.kslacker.fintech.dataaccess.repositories.api.UserRepository;
import ru.kslacker.fintech.dto.UserDto;
import ru.kslacker.fintech.mapping.UserMapper;
import ru.kslacker.fintech.models.user.Credentials;
import ru.kslacker.fintech.service.api.UserService;
import ru.kslacker.fintech.validation.service.api.ValidationService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ValidationService validator;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ValidationService validator, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto create(Credentials credentials) {
        validator.validate(credentials);

        User user = User.builder()
                .withUsername(credentials.username())
                .withPassword(passwordEncoder.encode(credentials.password()))
                .withEmail(credentials.email())
                .build();

        return UserMapper.asDto(userRepository.save(user));
    }
}
