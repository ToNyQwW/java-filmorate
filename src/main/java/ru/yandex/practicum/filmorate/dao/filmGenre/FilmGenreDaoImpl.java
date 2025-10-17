package ru.yandex.practicum.filmorate.dao.filmGenre;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.*;

@Repository
@AllArgsConstructor
public class FilmGenreDaoImpl implements FilmGenreDao {

    private static final String ADD_FILM_GENRES_SQL = """
                    INSERT INTO film_genre
                    (film_id, genre_id)
                    values (?, ?)
            """;

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

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;

    public void addFilmGenres(Long filmId, Set<Genre> genres) {
        jdbcTemplate.batchUpdate(ADD_FILM_GENRES_SQL, genres, genres.size(),
                (ps, genre) -> {
                    ps.setLong(1, filmId);
                    ps.setLong(2, genre.getId());
                }
        );
    }

    @Override
    public List<Genre> getFilmGenres(Long filmId) {
        List<Long> genreIds = jdbcTemplate.queryForList(GET_FILM_GENRES_SQL, Long.class, filmId);

        return genreDao.getGenresByListId(genreIds);
    }

    @Override
    public Map<Long, List<Genre>> getFilmsGenresByListFilmIds(List<Long> filmIds) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmsIds", filmIds);

        return namedParameterJdbcTemplate.query(GET_FILMS_GENRES_BY_LIST_FILMS_IDS_SQL, params, rs -> {
            Map<Long, List<Genre>> result = new HashMap<>();

            while (rs.next()) {
                var filmId = rs.getLong("film_id");
                var genreId = rs.getLong("genre_id");

                result.computeIfAbsent(filmId, value -> new ArrayList<>())
                        .add(genreDao.getGenreById(genreId).get());
            }

            return result;
        });
    }
}