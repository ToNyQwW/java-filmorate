package ru.yandex.practicum.filmorate.dao.likes.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetUserIdsByFilmIdsSql {

    private static final String GET_USER_IDS_BY_FILM_IDS_SQL = """
                    SELECT film_id,
                           user_id
                    FROM likes
                    WHERE film_id IN (:filmsIds)
            """;

    public static String create() {
        return GET_USER_IDS_BY_FILM_IDS_SQL;
    }
}