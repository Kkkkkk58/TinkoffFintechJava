package ru.kslacker.fintech.dataaccess.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import ru.kslacker.fintech.dataaccess.builders.OptionalInfoUserBuilder;
import ru.kslacker.fintech.dataaccess.builders.PasswordUserBuilder;
import ru.kslacker.fintech.dataaccess.builders.UsernameUserBuilder;
import ru.kslacker.fintech.dataaccess.enums.UserRole;
import ru.kslacker.fintech.exceptions.UserBuilderException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "roles", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles = new HashSet<>();

    protected User(
            String username,
            String email,
            String password,
            Set<UserRole> roles) {

        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public static class UserBuilder implements
            UsernameUserBuilder,
            PasswordUserBuilder,
            OptionalInfoUserBuilder {

        private String username;
        private String password;
        private String email = null;
        private Set<UserRole> roles = new HashSet<>();

        @Override
        public OptionalInfoUserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        @Override
        public PasswordUserBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        @Override
        public OptionalInfoUserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        @Override
        public OptionalInfoUserBuilder addRole(UserRole role) {
            this.roles.add(role);
            return this;
        }

        @Override
        public User build() {
            if (username == null || password == null) {
                throw UserBuilderException.missingCredentials();
            }
            if (roles.isEmpty()) {
                roles.add(UserRole.USER);
            }

            return new User(
                    username,
                    email,
                    password,
                    roles);
        }
    }
}