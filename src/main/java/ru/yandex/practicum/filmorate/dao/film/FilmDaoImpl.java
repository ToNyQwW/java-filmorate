package ru.yandex.practicum.filmorate.dao.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.filmGenre.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.likes.LikesDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.*;

@Slf4j
@Repository
@AllArgsConstructor
public class FilmDaoImpl implements FilmDao {

    private static final String ADD_FILM_SQL = """
            INSERT INTO film
            (name, description, release_date, duration, mpa_id)
            VALUES (:name, :description, :releaseDate, :duration, :mpaId)
            """;

    private static final String GET_FILM_WITH_MPA_SQL = """
             SELECT f.film_id,
                    f.name,
                    f.description,
                    f.release_date,
                    f.duration,
                    m.mpa_id,
                    m.name AS mpa_name
            FROM film f
            JOIN mpa m ON f.mpa_id = m.mpa_id
            WHERE f.film_id = ?
            """;

    private static final String GET_ALL_FILMS_WITH_MPA_SQL = """
            SELECT f.film_id,
                   f.name,
                   f.description,
                   f.release_date,
                   f.duration,
                   m.mpa_id,
                   m.name AS mpa_name
            FROM film f
            JOIN mpa m ON f.mpa_id = m.mpa_id
            """;

    private static final String GET_FILMS_BY_LIST_IDS_SQL = """
            SELECT f.film_id,
                   f.name,
                   f.description,
                   f.release_date,
                   f.duration,
                   m.mpa_id,
                   m.name AS mpa_name
            FROM film f
            JOIN mpa m ON f.mpa_id = m.mpa_id
            WHERE f.film_id IN (:filmsIds)
            """;

    private static final String UPDATE_FILM_SQL = """
            UPDATE film
                SET name = ?,
                    description = ?,
                    release_date = ?,
                    duration = ?,
                    mpa_id = ?
            WHERE film_id = ?;
            """;

    private final LikesDao likesDao;
    private final FilmGenreDao filmGenreDao;
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("releaseDate", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("mpaId", film.getMpa().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(ADD_FILM_SQL, params, keyHolder);

        film.setId(keyHolder.getKey().longValue());

        return film;
    }

    @Override
    public Optional<Film> getFilm(Long filmId) {
        var film = jdbcTemplate.queryForObject(GET_FILM_WITH_MPA_SQL, filmRowMapper, filmId);

        if (film != null) {
            var filmLikes = likesDao.getFilmLikes(filmId);
            var filmGenres = filmGenreDao.getFilmGenres(filmId);

            film.setLikes(new LinkedHashSet<>(filmLikes));
            film.setGenres(new LinkedHashSet<>(filmGenres));
        }
        return Optional.ofNullable(film);
    }

    @Override
    public List<Film> getFilms() {
        List<Film> films = jdbcTemplate.query(GET_ALL_FILMS_WITH_MPA_SQL, filmRowMapper);

        return buildFilms(films);
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        var popularFilmIds = likesDao.getPopularFilmIds(count);

        if (popularFilmIds.isEmpty()) {
            return Collections.emptyList();
        }

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmsIds", popularFilmIds);

        var films = namedParameterJdbcTemplate.query(GET_FILMS_BY_LIST_IDS_SQL, params, filmRowMapper);
        var result = buildFilms(films);

        result.sort(Comparator.comparingInt(film -> popularFilmIds.indexOf(film.getId())));

        return result;
    }

    private List<Film> buildFilms(List<Film> films) {

        var filmsIds = films.stream().map(Film::getId).toList();
        var userLikes = likesDao.getUserLikesByFilmIds(filmsIds);
        var filmsGenres = filmGenreDao.getFilmsGenresByListFilmIds(filmsIds);

        for (Film film : films) {
            var id = film.getId();
            film.setLikes(new HashSet<>(userLikes.getOrDefault(id, Collections.emptyList())));
            film.setGenres(new HashSet<>(filmsGenres.getOrDefault(id, Collections.emptyList())));
        }
        return films;
    }

    @Override
    public Film updateFilm(Film film) {
        int update = jdbcTemplate.update(UPDATE_FILM_SQL, film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (update == 0) {
            log.error("Failed to update film");
            throw new NotFoundException("Film with id " + film.getId() + " not found");
        }
        return film;
    }
}