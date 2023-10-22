package ru.kslacker.fintech.dataaccess.repositories.implementation.jdbc.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.kslacker.fintech.dataaccess.entities.WeatherTypeInfo;
import ru.kslacker.fintech.dataaccess.enums.WeatherType;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class WeatherTypeRowMapper implements RowMapper<WeatherTypeInfo> {
    @Override
    public WeatherTypeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        WeatherTypeInfo info = new WeatherTypeInfo();
        info.setId(rs.getLong("ID"));
        info.setType(WeatherType.valueOf(rs.getString("TYPE")));
        info.setIconUrl(rs.getString("ICON_URL"));
        return info;
    }
}
