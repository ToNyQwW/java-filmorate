package ru.yandex.practicum.filmorate.dao.directors.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetDirectorsByListIds {

    private static final String GET_DIRECTORS_BY_IDS_SQL = """
            SELECT director_id,
                   name
            FROM directors
            WHERE director_id in (:directorIds)
            """;

    public static String create() {
        return GET_DIRECTORS_BY_IDS_SQL;
    }
}
