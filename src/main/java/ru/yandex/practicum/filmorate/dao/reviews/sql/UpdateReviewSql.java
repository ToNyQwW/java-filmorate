package ru.yandex.practicum.filmorate.dao.reviews.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UpdateReviewSql {

    private static final String UPDATE_REVIEW_SQL = """
            UPDATE reviews
            SET content = ?,
                is_positive = ?,
                useful = ?
            WHERE review_id = ?
            """;

    public static String create() {
        return UPDATE_REVIEW_SQL;
    }
}
