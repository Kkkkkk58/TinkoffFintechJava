package ru.kslacker.fintech.dataaccess.repositories.api;

import ru.kslacker.fintech.dataaccess.entities.City;

import java.util.UUID;

public interface CityRepository {
    City save(City city);

    City getById(UUID cityId);

    void deleteById(UUID cityId);

    boolean existsById(UUID cityId);
}
