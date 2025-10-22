package ru.yandex.practicum.filmorate.dao.user.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetAllUsersSql {

    private static final String GET_ALL_USERS_SQL = """
            SELECT user_id,
                   email,
                   login,
                   name,
                   birthday
            FROM users
            """;

    public static String create() {
        return GET_ALL_USERS_SQL;
    }
}