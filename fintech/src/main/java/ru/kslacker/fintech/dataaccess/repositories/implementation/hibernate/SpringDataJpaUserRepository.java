package ru.kslacker.fintech.dataaccess.repositories.implementation.hibernate;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kslacker.fintech.dataaccess.entities.User;
import ru.kslacker.fintech.dataaccess.repositories.api.UserRepository;

@Repository
@Primary
public interface SpringDataJpaUserRepository extends JpaRepository<User, Long>, UserRepository {
}
