package ru.yandex.practicum.filmorate.dao.filmDirectors.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetFilmsByDirectorId {

    private static final String GET_FILMS_BY_DIRECTOR_ID_SQL = """
            SELECT film_id
            FROM film_directors
            WHERE director_id = ?
            """;

    public static String create() {
        return GET_FILMS_BY_DIRECTOR_ID_SQL;
    }
}
