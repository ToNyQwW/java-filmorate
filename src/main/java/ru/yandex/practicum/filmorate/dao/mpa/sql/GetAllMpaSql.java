package ru.yandex.practicum.filmorate.dao.mpa.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetAllMpaSql {

    private static final String GET_MPA_SQL = """
                    SELECT mpa_id as id,
                           name
                    FROM mpa
                    ORDER BY mpa_id
            """;

    public static String create() {
        return GET_MPA_SQL;
    }
}