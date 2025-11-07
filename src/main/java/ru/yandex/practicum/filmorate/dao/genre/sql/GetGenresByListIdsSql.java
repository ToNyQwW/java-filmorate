package ru.yandex.practicum.filmorate.dao.genre.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetGenresByListIdsSql {

    private static final String GET_GENRES_BY_LIST_IDS_SQL = """
                    SELECT genre_id,
                           name
                    FROM genre
                    WHERE genre_id IN (:ids)
            """;

    public static String create() {
        return GET_GENRES_BY_LIST_IDS_SQL;
    }
}