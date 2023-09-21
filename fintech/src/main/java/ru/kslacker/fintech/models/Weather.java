package ru.kslacker.fintech.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Weather {
    private UUID regionId;
    private String regionName;
    private int temperatureValue;
    private LocalDateTime dateTime;
}
