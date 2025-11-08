package ru.yandex.practicum.filmorate.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static ru.yandex.practicum.filmorate.constants.ValidateConstants.MIN_ID;

@Data
@Builder
@EqualsAndHashCode(of = "id")
public class Review {

    private Long id;

    @NotBlank
    private String content;

    private boolean isPositive;

    @Min(MIN_ID)
    private Long userId;

    @Min(MIN_ID)
    private Long filmId;

    private int useful;
}