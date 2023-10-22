package ru.kslacker.fintech.dataaccess.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import ru.kslacker.fintech.dataaccess.enums.WeatherType;

import java.util.Objects;

@Entity
@Table(name="weather_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WeatherTypeInfo {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private WeatherType type;

    private String iconUrl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        WeatherTypeInfo that = (WeatherTypeInfo) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
