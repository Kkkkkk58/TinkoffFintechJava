package ru.kslacker.fintech.dataaccess.builders;

import ru.kslacker.fintech.dataaccess.entities.User;
import ru.kslacker.fintech.dataaccess.enums.UserRole;

public interface OptionalInfoUserBuilder {
    OptionalInfoUserBuilder withEmail(String email);

    OptionalInfoUserBuilder addRole(UserRole role);

    User build();
}
