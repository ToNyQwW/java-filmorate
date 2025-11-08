package ru.yandex.practicum.filmorate.dao.reviewLikes.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RemoveLikeReviewSql {

    private static final String REMOVE_LIKE_REVIEW_SQL = """
            DELETE FROM review_likes
            WHERE user_id = ?
            AND
                review_id = ?
            AND
                is_positive = true
            """;

    public static String create() {
        return REMOVE_LIKE_REVIEW_SQL;
    }
}