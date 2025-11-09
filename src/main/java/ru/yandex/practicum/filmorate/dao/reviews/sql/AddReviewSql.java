package ru.yandex.practicum.filmorate.dao.reviews.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AddReviewSql {

    private static final String ADD_REVIEW_SQL = """
            INSERT INTO reviews
            (content, is_positive, user_id, film_id, useful)
            values (:content, :isPositive, :userId, :filmId, :useful)
            """;

    public static String create() {
        return ADD_REVIEW_SQL;
    }
}