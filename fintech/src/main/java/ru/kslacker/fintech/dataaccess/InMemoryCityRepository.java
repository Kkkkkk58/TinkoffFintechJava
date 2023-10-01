package ru.kslacker.fintech.dataaccess;

import org.springframework.stereotype.Repository;
import ru.kslacker.fintech.dataaccess.api.CityRepository;
import ru.kslacker.fintech.exceptions.CityAlreadyExistsException;
import ru.kslacker.fintech.exceptions.CityNotFoundException;
import ru.kslacker.fintech.models.City;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class InMemoryCityRepository implements CityRepository {
    private final Map<UUID, City> cities;

    public InMemoryCityRepository() {
        this.cities = new HashMap<>();
    }

    @Override
    public City createCity(City city) {
        if (exists(city.getId())) {
            throw new CityAlreadyExistsException(city.getId());
        }

        return cities.put(city.getId(), city);
    }

    @Override
    public City getById(UUID cityId) {
        if (!exists(cityId)) {
            throw new CityNotFoundException(cityId);
        }

        return cities.get(cityId);
    }

    @Override
    public void deleteById(UUID cityId) {
        if (!exists(cityId)) {
            throw new CityNotFoundException(cityId);
        }

        cities.remove(cityId);
    }

    @Override
    public boolean exists(UUID cityId) {
        return cities.containsKey(cityId);
    }
}
