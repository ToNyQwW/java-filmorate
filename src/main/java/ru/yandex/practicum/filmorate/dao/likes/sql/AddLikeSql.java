package ru.yandex.practicum.filmorate.dao.likes.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AddLikeSql {

    private static final String ADD_LIKE_SQL = """
                    INSERT INTO likes
                        (film_id, user_id)
                    VALUES (?, ?)
            """;

    public static String create() {
        return ADD_LIKE_SQL;
    }
}