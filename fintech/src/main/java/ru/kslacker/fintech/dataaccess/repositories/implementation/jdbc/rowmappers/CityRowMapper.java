package ru.kslacker.fintech.dataaccess.repositories.implementation.jdbc.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.kslacker.fintech.dataaccess.entities.City;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Component
public class CityRowMapper implements RowMapper<City> {
    @Override
    public City mapRow(ResultSet rs, int rowNum) throws SQLException {
        City city = new City();
        city.setId(rs.getObject("ID", UUID.class));
        city.setName(rs.getString("NAME"));
        return city;
    }
}
