package ru.yandex.practicum.filmorate.dao.filmDirectors.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetFilmsDirectorsByListFilmsIdsSql {

    private static final String GET_FILMS_DIRECTORS_BY_LIST_FILMS_IDS_SQL = """
            SELECT film_id,
                   director_id
            FROM film_directors
            WHERE film_id IN (:filmsIds)
            """;

    public static String create() {
        return GET_FILMS_DIRECTORS_BY_LIST_FILMS_IDS_SQL;
    }
}