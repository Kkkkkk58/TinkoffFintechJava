package ru.kslacker.fintech.dataaccess.repositories.api;

import ru.kslacker.fintech.dataaccess.entities.City;

import java.util.Optional;
import java.util.UUID;

public interface CityRepository {
    City save(City city);

    City getById(UUID cityId);

    Optional<City> findByName(String name);

    void deleteById(UUID cityId);

    boolean existsById(UUID cityId);
}
