package ru.kslacker.fintech.dataaccess.repositories.implementation.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.kslacker.fintech.dataaccess.entities.City;
import ru.kslacker.fintech.dataaccess.repositories.api.CityRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcCityRepository implements CityRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<City> rowMapper;

    public JdbcCityRepository(JdbcTemplate jdbcTemplate, RowMapper<City> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public City save(City city) {
        jdbcTemplate.update("INSERT INTO CITIES (ID, NAME) VALUES (?, ?)", city.getId(), city.getName());
        return city;
    }

    @Override
    public City getById(UUID cityId) {
        return jdbcTemplate.queryForObject("SELECT * FROM CITIES WHERE ID = ?", rowMapper, cityId);
    }

    @Override
    public Optional<City> findByName(String name) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject("SELECT * FROM CITIES WHERE NAME = ? LIMIT 1", rowMapper, name));
    }

    @Override
    public void deleteById(UUID cityId) {
        jdbcTemplate.update("DELETE FROM WEATHER WHERE CITY_ID = ?", cityId);
        jdbcTemplate.update("DELETE FROM CITIES WHERE ID = ?", cityId);
    }

    @Override
    public boolean existsById(UUID cityId) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM CITIES WHERE ID = ?", Integer.class, cityId);
        return count != null && count > 0;
    }
}
