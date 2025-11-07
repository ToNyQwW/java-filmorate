package ru.yandex.practicum.filmorate.dao.film.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AddFilmSql {

    private static final String ADD_FILM_SQL = """
            INSERT INTO film
            (name, description, release_date, duration, mpa_id)
            VALUES (:name, :description, :releaseDate, :duration, :mpaId)
            """;

    public static String create() {
        return ADD_FILM_SQL;
    }
}