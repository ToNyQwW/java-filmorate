package ru.yandex.practicum.filmorate.dao.likes.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetPopularFilmsIdSql {

    private static final String GET_POPULAR_FILMS_ID_SQL = """
                    SELECT film_id
                    FROM likes
                    GROUP BY film_id
                    ORDER BY COUNT(user_id) DESC
                    LIMIT ?
            """;

    public static String create() {
        return GET_POPULAR_FILMS_ID_SQL;
    }
}