package ru.yandex.practicum.filmorate.dao.reviewLikes.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AddLikeReviewSql {

    private static final String ADD_LIKE_REVIEW_SQL = """
            INSERT INTO review_likes
            (review_id, user_id, is_positive)
            VALUES (?, ?, true)
            """;

    public static String create() {
        return ADD_LIKE_REVIEW_SQL;
    }
}
