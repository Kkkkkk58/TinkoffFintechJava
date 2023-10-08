package ru.kslacker.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ConditionDto(
        String text,
        @JsonProperty("icon")
        String iconPath,
        int code
) {
}
