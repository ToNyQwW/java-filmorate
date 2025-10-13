package ru.yandex.practicum.filmorate.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class Mpa {

    private int id;

    @NotBlank
    private String name;
}
