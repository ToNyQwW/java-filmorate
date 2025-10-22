package ru.yandex.practicum.filmorate.dao.filmGenre.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetFilmGenresSql {

    private static final String GET_FILM_GENRES_SQL = """
                    SELECT genre_id
                    FROM film_genre
                    WHERE film_id = ?
            """;

    public static String create() {
        return GET_FILM_GENRES_SQL;
    }
}