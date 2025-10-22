package ru.yandex.practicum.filmorate.dao.friendship.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RemoveFriendshipSql {

    private static final String REMOVE_FRIENDSHIP_SQL = """
            DELETE FROM friendship
            WHERE user_id = ? AND friend_id = ?
            """;

    public static String create() {
        return REMOVE_FRIENDSHIP_SQL;
    }
}