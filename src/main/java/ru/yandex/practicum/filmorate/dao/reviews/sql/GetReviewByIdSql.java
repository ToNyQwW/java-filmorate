package ru.yandex.practicum.filmorate.dao.reviews.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetReviewByIdSql {

    private static final String GET_REVIEW_BY_ID_SQL = """
            SELECT review_id,
                   content,
                   is_positive,
                   user_id,
                   film_id,
                   useful
            FROM reviews
            WHERE review_id = ?
            """;

    public static String create() {
        return GET_REVIEW_BY_ID_SQL;
    }
}
