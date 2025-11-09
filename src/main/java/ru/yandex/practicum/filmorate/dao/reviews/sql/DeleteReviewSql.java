package ru.yandex.practicum.filmorate.dao.reviews.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DeleteReviewSql {

    private static final String DELETE_REVIEW_SQL = """
            DELETE FROM reviews
            WHERE review_id = ?
            """;

    public static String create() {
        return DELETE_REVIEW_SQL;
    }
}
