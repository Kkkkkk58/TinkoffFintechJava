package ru.kslacker.fintech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ConditionDto(
        String text,
        @JsonProperty("icon")
        String iconPath,
        int code
) {
}
