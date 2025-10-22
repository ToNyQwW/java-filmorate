package ru.yandex.practicum.filmorate.dao.user.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UpdateUserSql {

    private static final String UPDATE_USER_SQL = """
            UPDATE users
            SET email = ?,
                login = ?,
                name = ?,
                birthday = ?
            WHERE user_id = ?
            """;

    public static String create() {
        return UPDATE_USER_SQL;
    }
}