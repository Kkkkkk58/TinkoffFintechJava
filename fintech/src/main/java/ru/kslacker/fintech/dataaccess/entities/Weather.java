package ru.kslacker.fintech.dataaccess.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="weather")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id = UUID.randomUUID();

    @ManyToOne
    private City city;

    @ManyToOne
    private WeatherTypeInfo type;

    private double temperatureValue;
    private LocalDateTime dateTime;

    public Weather(City city, WeatherTypeInfo type, double temperatureValue, LocalDateTime dateTime) {
        this.city = city;
        this.type = type;
        this.temperatureValue = temperatureValue;
        this.dateTime = dateTime;
    }

    public UUID getCityId() {
        return city.getId();
    }

    public String getCityName() {
        return city.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Weather weather = (Weather) o;
        return getId() != null && Objects.equals(getId(), weather.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
