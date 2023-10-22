package ru.kslacker.fintech.dataaccess.repositories.implementation.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.kslacker.fintech.dataaccess.entities.WeatherTypeInfo;
import ru.kslacker.fintech.dataaccess.enums.WeatherType;
import ru.kslacker.fintech.dataaccess.repositories.api.WeatherTypeInfoRepository;

@Repository
public class JdbcWeatherTypeInfoRepository implements WeatherTypeInfoRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<WeatherTypeInfo> rowMapper;

    public JdbcWeatherTypeInfoRepository(JdbcTemplate jdbcTemplate, RowMapper<WeatherTypeInfo> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public WeatherTypeInfo getByType(WeatherType type) {
        return jdbcTemplate.queryForObject("SELECT * FROM WEATHER_TYPE WHERE TYPE=?", rowMapper, type.name());
    }
}
