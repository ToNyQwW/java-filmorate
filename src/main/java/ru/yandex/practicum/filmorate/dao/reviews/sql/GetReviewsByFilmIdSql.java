package ru.yandex.practicum.filmorate.dao.reviews.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetReviewsByFilmIdSql {

    private static final String GET_REVIEWS_BY_FILM_ID_SQL = """
            SELECT review_id,
                   content,
                   is_positive,
                   user_id,
                   film_id,
                   useful
            FROM reviews
            WHERE film_id = ?
            ORDER BY useful DESC
            LIMIT ?
            """;

    public static String create() {
        return GET_REVIEWS_BY_FILM_ID_SQL;
    }
}
