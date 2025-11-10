package ru.yandex.practicum.filmorate.dao.likes.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetMostCommonUserByLikesSql {

    private static final String GET_MOST_COMMON_USER_BY_LIKES_SQL = """
            SELECT l2.user_id,
                   COUNT(*)
            FROM likes l1
            JOIN likes l2 ON (l1.user_id = l2.user_id)
            WHERE l1.user_id = ? AND l2.user_id != ?
            GROUP BY l2.user_id
            ORDER BY COUNt(*) DESC
            LIMIT 1
            """;

    public static String create() {
        return GET_MOST_COMMON_USER_BY_LIKES_SQL;
    }
}
