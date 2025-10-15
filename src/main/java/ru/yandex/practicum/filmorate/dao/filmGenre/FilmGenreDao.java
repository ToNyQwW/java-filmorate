package ru.yandex.practicum.filmorate.dao.filmGenre;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.entity.FilmGenres;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenreDao {

    private static final String GET_FILM_GENRES_SQL = """
                    SELECT genre_id
                    FROM film_genre
                    WHERE film_id = ?
            """;

    private static final String GET_FILMS_GENRES_BY_LIST_FILMS_IDS_SQL = """
                    SELECT film_id,
                          genre_id
                    FROM film_genre
                    WHERE film_id IN (:filmsIds)
            """;

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final GenreDao genreDao;

    public FilmGenres getFilmGenres(Long filmId) {
        List<Long> genreIds = jdbcTemplate.query(GET_FILM_GENRES_SQL, (rs, rowNum) -> rs.getLong("genre_id"), filmId);

        return new FilmGenres(filmId, genreDao.getGenresByListId(genreIds));
    }

    public Map<Long, FilmGenres> getFilmsGenresByListFilmIds(List<Long> filmIds) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmsIds", filmIds);

        return namedParameterJdbcTemplate.query(GET_FILMS_GENRES_BY_LIST_FILMS_IDS_SQL, params, rs -> {
            Map<Long, FilmGenres> result = new HashMap<>();

            while (rs.next()) {
                var filmId = rs.getLong("film_id");
                var genreId = rs.getLong("genre_id");

                result.computeIfAbsent(filmId, value -> new FilmGenres(filmId, new ArrayList<>()))
                        .getGenres().add(genreDao.getGenreById(genreId));
            }

            return result;
        });
    }
}