package ru.yandex.practicum.filmorate.dao.likes.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RemoveLikeSql {

    private static final String REMOVE_LIKE_SQL = """
                    DELETE FROM likes
                    WHERE film_id = ? AND user_id = ?
            """;

    public static String create() {
        return REMOVE_LIKE_SQL;
    }
}