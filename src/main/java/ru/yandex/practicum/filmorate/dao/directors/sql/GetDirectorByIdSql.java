package ru.yandex.practicum.filmorate.dao.directors.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetDirectorByIdSql {

    private static final String GET_DIRECTOR_BY_ID_SQL = """
            SELECT director_id,
                   name
            FROM directors
            WHERE director_id = ?
            """;

    public static String create() {
        return GET_DIRECTOR_BY_ID_SQL;
    }
}
