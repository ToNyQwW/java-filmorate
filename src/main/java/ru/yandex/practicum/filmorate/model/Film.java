package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.Validation.positiveDuration.PositiveDuration;
import ru.yandex.practicum.filmorate.Validation.minReleaseDate.MinReleaseDate;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {

    private int id;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 200)
    private String description;

    @NotNull
    @MinReleaseDate
    private LocalDate releaseDate;

    @NotNull
    @PositiveDuration
    private Duration duration;
}
