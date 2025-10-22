package ru.yandex.practicum.filmorate.dao.filmGenre.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetFilmsGenresByListFilmsIdsSql {

    private static final String GET_FILMS_GENRES_BY_LIST_FILMS_IDS_SQL = """
                    SELECT film_id,
                          genre_id
                    FROM film_genre
                    WHERE film_id IN (:filmsIds)
            """;

    public static String create() {
        return GET_FILMS_GENRES_BY_LIST_FILMS_IDS_SQL;
    }
}