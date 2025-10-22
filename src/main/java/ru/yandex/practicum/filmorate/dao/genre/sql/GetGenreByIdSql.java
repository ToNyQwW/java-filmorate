package ru.yandex.practicum.filmorate.dao.genre.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetGenreByIdSql {

    private static final String GET_GENRE_BY_ID_SQL = """
                    SELECT genre_id,
                           name
                    FROM genre
                    WHERE genre_id = ?
            """;

    public static String create() {
        return GET_GENRE_BY_ID_SQL;
    }
}