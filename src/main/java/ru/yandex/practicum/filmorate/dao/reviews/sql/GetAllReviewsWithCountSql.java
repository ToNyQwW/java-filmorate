package ru.yandex.practicum.filmorate.dao.reviews.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetAllReviewsWithCountSql {

    private static final String GET_ALL_REVIEWS_WITH_COUNT_SQL = """
            SELECT review_id,
                   content,
                   is_positive,
                   user_id,
                   film_id,
                   useful
            FROM reviews
            ORDER BY useful DESC
            LIMIT ?
            """;

    public static String create() {
        return GET_ALL_REVIEWS_WITH_COUNT_SQL;
    }
}