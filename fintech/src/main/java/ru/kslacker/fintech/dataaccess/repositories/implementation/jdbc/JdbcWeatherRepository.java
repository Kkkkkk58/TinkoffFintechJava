package ru.kslacker.fintech.dataaccess.repositories.implementation.jdbc;

import lombok.NonNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.kslacker.fintech.dataaccess.entities.Weather;
import ru.kslacker.fintech.dataaccess.repositories.api.WeatherRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public class JdbcWeatherRepository implements WeatherRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Weather> rowMapper;
    private final String selectWeatherStatement =
            """
            SELECT W.*, C.NAME AS CITY_NAME, WT.TYPE AS WEATHER_TYPE, WT.ICON_URL AS ICON_URL
            FROM WEATHER W
            JOIN WEATHER_TYPE WT on WT.ID = W.TYPE_ID
            JOIN CITIES C on C.ID = W.CITY_ID
            """;

    public JdbcWeatherRepository(JdbcTemplate jdbcTemplate, RowMapper<Weather> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<Weather> findAll() {
        return jdbcTemplate.query(selectWeatherStatement, rowMapper);
    }

    @Override
    public List<Weather> getByCityIdAndDateTimeBetween(UUID cityId, LocalDateTime from, LocalDateTime to) {
        return jdbcTemplate.query(selectWeatherStatement + " WHERE CITY_ID = ? AND DATE_TIME BETWEEN ? AND ?",
                rowMapper, cityId, from, to);
    }

    @Override
    public void createForCity(UUID cityId, List<Weather> weather) {
        jdbcTemplate.batchUpdate("INSERT INTO WEATHER (ID, CITY_ID, TYPE_ID, TEMPERATURE_VALUE, DATE_TIME) VALUES (?,?,?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                        Weather item = weather.get(i);
                        ps.setString(1, item.getId().toString());
                        ps.setString(2, item.getCityId().toString());
                        ps.setString(3, item.getType().getId().toString());
                        ps.setDouble(4, item.getTemperatureValue());
                        ps.setTimestamp(5, Timestamp.valueOf(item.getDateTime()));
                    }

                    @Override
                    public int getBatchSize() {
                        return weather.size();
                    }
                });
    }

    @Override
    public void updateWeather(Weather weather) {
        jdbcTemplate.update("MERGE INTO WEATHER KEY (ID) VALUES (?, ?, ?, ?, ?)",
                weather.getId(), weather.getTemperatureValue(), weather.getDateTime(), weather.getType().getId(), weather.getCityId());
    }

    @Override
    public void deleteByCityId(UUID cityId) {
        jdbcTemplate.update("DELETE FROM WEATHER WHERE CITY_ID=?", cityId);
    }
}
