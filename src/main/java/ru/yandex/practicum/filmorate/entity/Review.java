package ru.yandex.practicum.filmorate.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static ru.yandex.practicum.filmorate.constants.ValidateConstants.MIN_ID;

@Data
@Builder
@EqualsAndHashCode(of = "reviewId")
public class Review {

    private Long reviewId;

    @NotBlank
    private String content;

    @JsonProperty("isPositive")
    @NotNull
    private Boolean isPositive;

    @NotNull
    private Long userId;

    @NotNull
    private Long filmId;

    private int useful;
}