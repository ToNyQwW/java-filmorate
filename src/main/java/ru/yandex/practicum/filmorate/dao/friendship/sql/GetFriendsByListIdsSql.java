package ru.yandex.practicum.filmorate.dao.friendship.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetFriendsByListIdsSql {

    private static final String GET_FRIENDS_BY_LIST_IDS_SQL = """
            SELECT user_id,
                 friend_id
            FROM friendship
            WHERE user_id IN (:userIds)
            """;

    public static String create() {
        return GET_FRIENDS_BY_LIST_IDS_SQL;
    }
}