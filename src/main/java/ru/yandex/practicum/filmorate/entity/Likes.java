package ru.yandex.practicum.filmorate.entity;

import lombok.Data;

@Data
public class Likes {

    private Long filmId;

    private Long userId;
}
