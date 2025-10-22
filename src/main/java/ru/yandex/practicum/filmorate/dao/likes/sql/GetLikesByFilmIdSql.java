package ru.yandex.practicum.filmorate.dao.likes.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetLikesByFilmIdSql {

    private static final String GET_LIKES_BY_FILM_ID_SQL = """
                    SELECT user_id
                    FROM likes
                    WHERE film_id = ?
            """;

    public static String create() {
        return GET_LIKES_BY_FILM_ID_SQL;
    }
}