package ru.yandex.practicum.filmorate.dao.directors.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UpdateDirectorSql {

    private static final String UPDATE_DIRECTOR_SQL = """
            UPDATE directors
            SET  name = ?
            WHERE director_id = ?
            """;

    public static String create() {
        return UPDATE_DIRECTOR_SQL;
    }
}
