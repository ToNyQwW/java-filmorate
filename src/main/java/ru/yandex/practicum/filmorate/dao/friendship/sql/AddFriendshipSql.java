package ru.yandex.practicum.filmorate.dao.friendship.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AddFriendshipSql {

    private static final String ADD_FRIENDSHIP_SQL = """
            INSERT INTO friendship
                (user_id, friend_id)
            VALUES (?, ?)
            """;

    public static String create() {
        return ADD_FRIENDSHIP_SQL;
    }
}