package ru.yandex.practicum.filmorate.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(of = "id")
public class Genre {

    private Long id;

    @NotBlank
    private String name;
}
