package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "id")
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

    private Set<Integer> friends = new HashSet<>();

    public void addFriend(int id) {
        this.friends.add(id);
    }

    public void removeFriend(int id) {
        this.friends.remove(id);
    }
}
