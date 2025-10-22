package ru.yandex.practicum.filmorate.dao.user.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetUsersByListIdsSql {

    private static final String GET_USERS_BY_LIST_IDS_SQL = """
            SELECT user_id,
                   email,
                   login,
                   name,
                   birthday
            FROM users
            WHERE user_id IN (:userIds)
            """;

    public static String create() {
        return GET_USERS_BY_LIST_IDS_SQL;
    }
}