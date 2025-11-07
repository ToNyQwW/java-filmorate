package ru.yandex.practicum.filmorate.dao.genre.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetGenresSql {

    private static final String GET_GENRES_SQL = """
                    SELECT genre_id,
                           name
                    FROM genre
            """;

    public static String create() {
        return GET_GENRES_SQL;
    }
}