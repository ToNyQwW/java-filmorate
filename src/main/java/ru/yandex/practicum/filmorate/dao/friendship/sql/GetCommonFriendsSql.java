package ru.yandex.practicum.filmorate.dao.friendship.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetCommonFriendsSql {

    private static final String GET_COMMON_FRIENDS_SQL = """
            SELECT f1.friend_id
            FROM friendship f1
            INNER JOIN friendship f2 ON f1.friend_id = f2.friend_id
            WHERE f1.user_id = ? AND f2.user_id = ?
            """;

    public static String create() {
        return GET_COMMON_FRIENDS_SQL;
    }
}