package ru.yandex.practicum.filmorate.dao.directors.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AddDirectorSql {

    private static final String ADD_DIRECTOR_SQL = """
            INSERT INTO directors (name)
            VALUES (:name)
            """;

    public static String create() {
        return ADD_DIRECTOR_SQL;
    }
}