package ru.yandex.practicum.filmorate.dao.friendship.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetFriendsByIdSql {

    private static final String GET_FRIENDS_BY_ID_SQL = """
            SELECT friend_id
            FROM friendship
            WHERE user_id = ?
            """;

    public static String create() {
        return GET_FRIENDS_BY_ID_SQL;
    }
}