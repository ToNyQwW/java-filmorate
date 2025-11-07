package ru.yandex.practicum.filmorate.dao.filmGenre.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AddFilmGenresSql {

    private static final String ADD_FILM_GENRES_SQL = """
                    INSERT INTO film_genre
                    (film_id, genre_id)
                    values (?, ?)
            """;

    public static String create() {
        return ADD_FILM_GENRES_SQL;
    }
}