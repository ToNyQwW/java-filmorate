package ru.yandex.practicum.filmorate.dao.reviewLikes.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CheckReactionSql {

    private static final String CHECK_REACTION_SQL = """
            SELECT COUNT(*)
            FROM review_likes
            WHERE review_id = ?
              AND
                    user_id = ?
              AND
                is_positive = ?
            """;

    public static String create() {
        return CHECK_REACTION_SQL;
    }
}
