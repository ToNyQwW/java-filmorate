package ru.yandex.practicum.filmorate.dao.film;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.filmGenre.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.likes.LikesDao;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
@AllArgsConstructor
public class FilmDaoImpl implements FilmDao {

    private static final String ADD_FILM_SQL = """
            INSERT INTO film
            (name, description, release_date, duration, mpa_id)
            VALUES (?, ?, ?, ?, ?)
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

    private final FilmRowMapper filmRowMapper;

    private final LikesDao likesDao;

    private final FilmGenreDao filmGenreDao;

    private final MpaDao mpaDao;

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(ADD_FILM_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        film.setId(keyHolder.getKey().longValue());

        return film;
    }

    @Override
    public Optional<Film> getFilm(Long filmId) {
        var film = jdbcTemplate.queryForObject(GET_FILM_WITH_MPA_SQL, filmRowMapper, filmId);

        if (film == null) {
            return Optional.empty();
        }
        var filmLikes = likesDao.getFilmLikes(filmId);
        film.setLikes(new LinkedHashSet<>(filmLikes));
        film.setGenres(new LinkedHashSet<>(filmGenreDao.getFilmGenres(filmId)));

        return Optional.of(film);
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
        var builtFilms = buildFilms(films);

        builtFilms.sort(Comparator.comparingInt(f -> popularFilmIds.indexOf(f.getId())));

        return builtFilms;
    }

    private List<Film> buildFilms(List<Film> films) {

        var filmsIds = films.stream().map(Film::getId).toList();
        var userLikes = likesDao.getUserLikesByFilmIds(filmsIds);
        var filmsGenres = filmGenreDao.getFilmsGenresByListFilmIds(filmsIds);

        for (Film film : films) {
            var id = film.getId();
            film.setLikes(new LinkedHashSet<>(userLikes.get(id)));
            film.setGenres(new LinkedHashSet<>(filmsGenres.get(id)));
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
            throw new NotFoundException("Film with id " + film.getId() + " not found");
        }

        return film;
    }
}
