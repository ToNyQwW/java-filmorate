package ru.yandex.practicum.filmorate.dao.user.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetUserSql {

    private static final String GET_USER_SQL = """
            SELECT user_id,
                   email,
                   login,
                   name,
                   birthday
            FROM users
            WHERE user_id = ?
            """;

    public static String create() {
        return GET_USER_SQL;
    }
}