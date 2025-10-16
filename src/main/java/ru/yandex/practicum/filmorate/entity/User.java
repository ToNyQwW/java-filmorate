package ru.yandex.practicum.filmorate.entity;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode(of = "id")
public class User {

    private Long id;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^\\S+$")
    private String login;

    private String name;

    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    private List<Long> friends;
}
