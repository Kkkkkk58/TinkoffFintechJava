package ru.kslacker.fintech.dataaccess.repositories.implementation.hibernate;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kslacker.fintech.dataaccess.entities.WeatherTypeInfo;
import ru.kslacker.fintech.dataaccess.repositories.api.WeatherTypeInfoRepository;

@Repository
@Primary
public interface SpringDataJpaWeatherTypeInfoRepository extends JpaRepository<WeatherTypeInfo, Long>, WeatherTypeInfoRepository {
}
