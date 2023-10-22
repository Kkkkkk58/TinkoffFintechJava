package ru.kslacker.fintech.dataaccess.repositories.implementation.jdbc.rowmappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.kslacker.fintech.dataaccess.entities.City;
import ru.kslacker.fintech.dataaccess.entities.Weather;
import ru.kslacker.fintech.dataaccess.entities.WeatherTypeInfo;
import ru.kslacker.fintech.dataaccess.enums.WeatherType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class WeatherRowMapper implements RowMapper<Weather> {
    @Override
    public Weather mapRow(ResultSet rs, int rowNum) throws SQLException {
        Weather weather = new Weather();
        weather.setId(rs.getObject("ID", UUID.class));
        weather.setDateTime(rs.getObject("DATE_TIME", LocalDateTime.class));
        weather.setTemperatureValue(rs.getDouble("TEMPERATURE_VALUE"));
        WeatherTypeInfo weatherTypeInfo = getWeatherTypeInfo(rs);
        weather.setType(weatherTypeInfo);
        City city = getCity(rs);
        weather.setCity(city);
        return weather;
    }

    private WeatherTypeInfo getWeatherTypeInfo(ResultSet rs) throws SQLException {
        WeatherTypeInfo weatherTypeInfo = new WeatherTypeInfo();
        weatherTypeInfo.setId(rs.getLong("TYPE_ID"));
        weatherTypeInfo.setType(WeatherType.valueOf(rs.getString("WEATHER_TYPE")));
        weatherTypeInfo.setIconUrl(rs.getString("ICON_URL"));
        return weatherTypeInfo;
    }

    private City getCity(ResultSet rs) throws SQLException {
        City city = new City();
        city.setId(rs.getObject("CITY_ID", UUID.class));
        city.setName(rs.getString("CITY_NAME"));
        return city;
    }
}
