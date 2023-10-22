package ru.kslacker.fintech.dataaccess.repositories.implementation.hibernate;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kslacker.fintech.dataaccess.entities.City;
import ru.kslacker.fintech.dataaccess.repositories.api.CityRepository;

import java.util.UUID;

@Repository
@Primary
public interface SpringDataJpaCityRepository extends JpaRepository<City, UUID>, CityRepository {
}
