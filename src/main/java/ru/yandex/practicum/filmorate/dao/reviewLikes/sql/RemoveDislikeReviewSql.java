package ru.yandex.practicum.filmorate.dao.reviewLikes.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RemoveDislikeReviewSql {

    private static final String REMOVE_DISLIKE_REVIEW_SQL = """
            DELETE FROM review_likes
            WHERE review_id = ?
            AND
                user_id = ?
            AND
                is_positive = false
            """;

    public static String create() {
        return REMOVE_DISLIKE_REVIEW_SQL;
    }
}
