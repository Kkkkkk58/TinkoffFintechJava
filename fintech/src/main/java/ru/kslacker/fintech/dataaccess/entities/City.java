package ru.kslacker.fintech.dataaccess.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "cities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class City {
    @Id
    private UUID id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Weather> weather = new ArrayList<>();

    public City(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addWeather(Weather w) {
        weather.add(w);
    }

    public void deleteWeather(Weather w) {
        weather.remove(w);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        City city = (City) o;
        return getId() != null && Objects.equals(getId(), city.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
