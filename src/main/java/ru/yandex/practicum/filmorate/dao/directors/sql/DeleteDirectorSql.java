package ru.yandex.practicum.filmorate.dao.directors.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DeleteDirectorSql {

    private static final String DELETE_DIRECTOR_SQL = """
            DELETE FROM directors
            WHERE director_id = ?
            """;

    public static String create() {
        return DELETE_DIRECTOR_SQL;
    }
}
