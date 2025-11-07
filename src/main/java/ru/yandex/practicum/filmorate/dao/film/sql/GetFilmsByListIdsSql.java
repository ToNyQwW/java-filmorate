package ru.yandex.practicum.filmorate.dao.film.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetFilmsByListIdsSql {

    private static final String GET_FILMS_BY_LIST_IDS_SQL = """
            SELECT f.film_id,
                   f.name,
                   f.description,
                   f.release_date,
                   f.duration,
                   m.mpa_id,
                   m.name AS mpa_name
            FROM film f
            JOIN mpa m ON f.mpa_id = m.mpa_id
            WHERE f.film_id IN (:filmsIds)
            """;

    public static String create() {
        return GET_FILMS_BY_LIST_IDS_SQL;
    }
}