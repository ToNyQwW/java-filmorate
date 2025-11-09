package ru.yandex.practicum.filmorate.dao.filmDirectors.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AddFilmDirectorSql {

    private static final String ADD_FILM_DIRECTOR_SQL = """
                    INSERT INTO film_directors
                    (film_id, director_id)
                    values (?, ?)
            """;

    public static String create() {
        return ADD_FILM_DIRECTOR_SQL;
    }
}
