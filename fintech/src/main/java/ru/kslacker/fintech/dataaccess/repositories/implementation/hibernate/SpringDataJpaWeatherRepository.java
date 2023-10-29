package ru.kslacker.fintech.dataaccess.repositories.implementation.hibernate;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kslacker.fintech.dataaccess.entities.Weather;
import ru.kslacker.fintech.dataaccess.repositories.api.WeatherRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@Primary
public interface SpringDataJpaWeatherRepository extends JpaRepository<Weather, UUID>, WeatherRepository {

    @Override
    @Query(value = "select w from Weather w where w.city.id =:cityId and w.dateTime between :from and :to")
    List<Weather> getByCityIdAndDateTimeBetween(UUID cityId, LocalDateTime from, LocalDateTime to);

    @Override
    default void createForCity(UUID cityId, List<Weather> weather) {
        saveAll(weather);
    }

    @Override
    @Modifying
    @Query(value = """
            update Weather w
            set w.temperatureValue = :#{#weather.getTemperatureValue()}
            where w.city.id = :#{#weather.getCityId()}
                and w.dateTime = :#{#weather.getDateTime()}
            """)
    void updateWeather(@Param("weather") Weather weather);

    @Override
    @Modifying
    @Query(value = "delete from Weather w where w.city.id = :cityId")
    void deleteByCityId(UUID cityId);
}
