package ru.yandex.practicum.filmorate.dao.likes.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetRecommendedFilmsBySimilarUserSql {

    private static final String GET_RECOMMENDED_FILMS_BY_SIMILAR_USER_SQL = """
            SELECT l.film_id
            FROM likes l
            WHERE l.user_id = ?
                AND l.film_id NOT IN (
                    SELECT film_id
                    FROM likes
                    WHERE user_id = ?
                )
            """;

    public static String create() {
        return GET_RECOMMENDED_FILMS_BY_SIMILAR_USER_SQL;
    }
}
