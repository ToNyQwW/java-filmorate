package ru.yandex.practicum.filmorate.dao.filmDirectors.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetFilmDirectorsSql {

    private static final String GET_FILM_DIRECTORS_SQL = """
                    SELECT director_id
                    FROM film_directors
                    WHERE film_id = ?
            """;

    public static String create() {
        return GET_FILM_DIRECTORS_SQL;
    }
}
