package ru.yandex.practicum.filmorate.dao.reviews.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IncreaseUsefulReviewSql {

    private static final String INCREASE_USEFUL_REVIEW_SQL = """
            UPDATE reviews
            SET useful = useful + ?
            WHERE review_id = ?
            """;

    public static String create() {
        return INCREASE_USEFUL_REVIEW_SQL;
    }
}
