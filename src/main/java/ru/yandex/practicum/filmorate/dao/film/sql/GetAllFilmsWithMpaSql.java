package ru.yandex.practicum.filmorate.dao.film.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetAllFilmsWithMpaSql {

    private static final String GET_ALL_FILMS_WITH_MPA_SQL = """
            SELECT f.film_id,
                   f.name,
                   f.description,
                   f.release_date,
                   f.duration,
                   m.mpa_id,
                   m.name AS mpa_name
            FROM film f
            JOIN mpa m ON f.mpa_id = m.mpa_id
            """;

    public static String create() {
        return GET_ALL_FILMS_WITH_MPA_SQL;
    }
}