package ru.yandex.practicum.filmorate.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"userId", "friendId"})
public class Friendship {

    private Long userId;

    private Long friendId;

    private boolean confirmed;
}
