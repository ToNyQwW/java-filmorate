package ru.yandex.practicum.filmorate.dao.film.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UpdateFilmSql {

    private static final String UPDATE_FILM_SQL = """
            UPDATE film
                SET name = ?,
                    description = ?,
                    release_date = ?,
                    duration = ?,
                    mpa_id = ?
            WHERE film_id = ?;
            """;

    public static String create() {
        return UPDATE_FILM_SQL;
    }
}