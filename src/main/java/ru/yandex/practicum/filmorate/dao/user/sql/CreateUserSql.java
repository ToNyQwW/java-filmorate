package ru.yandex.practicum.filmorate.dao.user.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CreateUserSql {

    private static final String CREATE_USER_SQL = """
            INSERT INTO users
                (email, login, name, birthday)
            VALUES (:email, :login, :name, :birthday)
            """;

    public static String create() {
        return CREATE_USER_SQL;
    }
}