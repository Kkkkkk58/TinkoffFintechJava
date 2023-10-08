package ru.kslacker.fintech.dataaccess.api;

import ru.kslacker.fintech.models.City;

import java.util.UUID;

public interface CityRepository {
    City createCity(City city);

    City getById(UUID cityId);

    void deleteById(UUID cityId);

    boolean exists(UUID cityId);
}
