package ru.yandex.practicum.filmorate.dao.reviews.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DecreaseUsefulReviewSql {

    private static final String DECREASE_USEFUL_REVIEW_SQL = """
            UPDATE reviews
            SET useful = useful - 1
            WHERE review_id = ?
            """;

    public static String create() {
        return DECREASE_USEFUL_REVIEW_SQL;
    }
}
