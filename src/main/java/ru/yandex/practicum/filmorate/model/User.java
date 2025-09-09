package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private int id;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^\\S+$")
    private String login;

    private String name;

    @NotNull
    @PastOrPresent
    private LocalDate birthday;
}
