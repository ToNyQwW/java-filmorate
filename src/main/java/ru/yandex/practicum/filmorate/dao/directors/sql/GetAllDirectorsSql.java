package ru.yandex.practicum.filmorate.dao.directors.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetAllDirectorsSql {

    private static final String GET_ALL_DIRECTORS_SQL = """
            SELECT director_id,
                   name
            FROM directors
            """;

    public static String create() {
        return GET_ALL_DIRECTORS_SQL;
    }
}
