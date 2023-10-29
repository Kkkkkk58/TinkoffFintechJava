package ru.kslacker.fintech.dataaccess.repositories.implementation.inmemory;

import org.springframework.stereotype.Repository;
import ru.kslacker.fintech.dataaccess.repositories.api.CityRepository;
import ru.kslacker.fintech.exceptions.CityAlreadyExistsException;
import ru.kslacker.fintech.exceptions.CityNotFoundException;
import ru.kslacker.fintech.dataaccess.entities.City;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InMemoryCityRepository implements CityRepository {
    private final Map<UUID, City> cities;

    public InMemoryCityRepository() {
        this.cities = new HashMap<>();
    }

    @Override
    public City save(City city) {
        if (existsById(city.getId())) {
            throw new CityAlreadyExistsException(city.getId());
        }

        return cities.put(city.getId(), city);
    }

    @Override
    public City getById(UUID cityId) {
        if (!existsById(cityId)) {
            throw new CityNotFoundException(cityId);
        }

        return cities.get(cityId);
    }

    @Override
    public Optional<City> findByName(String name) {
        return cities.values().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst();
    }

    @Override
    public void deleteById(UUID cityId) {
        if (!existsById(cityId)) {
            throw new CityNotFoundException(cityId);
        }

        cities.remove(cityId);
    }

    @Override
    public boolean existsById(UUID cityId) {
        return cities.containsKey(cityId);
    }
}
