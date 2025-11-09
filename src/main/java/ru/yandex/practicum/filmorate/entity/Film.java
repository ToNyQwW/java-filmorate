package ru.yandex.practicum.filmorate.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validation.minReleaseDate.MinReleaseDate;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(of = "id")
public class Film {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 200)
    private String description;

    @NotNull
    @MinReleaseDate
    private LocalDate releaseDate;

    @Positive
    private int duration;

    private Set<Long> likes;

    private Set<Genre> genres;

    private Set<Director> directors;

    private Mpa mpa;
}
