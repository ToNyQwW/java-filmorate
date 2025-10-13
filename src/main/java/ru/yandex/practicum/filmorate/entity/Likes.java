package ru.yandex.practicum.filmorate.entity;

import lombok.Data;

@Data
public class Likes {

    private Long film_id;

    private Long user_id;
}
