package ru.yandex.practicum.filmorate.dao.mpa.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetMpaByIdSql {

    private static final String GET_MPA_BY_ID_SQL = """
                    SELECT mpa_id as id,
                           name
                    FROM mpa
                    WHERE mpa_id = ?
            """;

    public static String create() {
        return GET_MPA_BY_ID_SQL;
    }
}