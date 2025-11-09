package ru.yandex.practicum.filmorate.dao.reviewLikes.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AddDislikeReviewSql {

    private static final String ADD_DISLIKE_REVIEW_SQL = """
            INSERT INTO review_likes
            (review_id, user_id, is_positive)
            VALUES (?, ?, false)
            """;

    public static String create() {
        return ADD_DISLIKE_REVIEW_SQL;
    }
}
