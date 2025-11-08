package ru.yandex.practicum.filmorate.dao.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.film.sql.*;
import ru.yandex.practicum.filmorate.dao.filmGenre.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.likes.LikesDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.*;

@Slf4j
@Repository
@AllArgsConstructor
public class FilmDaoImpl implements FilmDao {

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
        namedParameterJdbcTemplate.update(AddFilmSql.create(), params, keyHolder);

        film.setId(keyHolder.getKey().longValue());

        return film;
    }

    @Override
    public Optional<Film> getFilm(Long filmId) {
        var result = jdbcTemplate.query(GetFilmWithMpaSql.create(), filmRowMapper, filmId);
        var film = DataAccessUtils.singleResult(result);

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
        List<Film> films = jdbcTemplate.query(GetAllFilmsWithMpaSql.create(), filmRowMapper);

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

        var films = namedParameterJdbcTemplate.query(GetFilmsByListIdsSql.create(), params, filmRowMapper);
        var result = buildFilms(films);

        sortFilmsByPopularityOrder(result, popularFilmIds);

        return result;
    }

    private void sortFilmsByPopularityOrder(List<Film> films, List<Long> popularFilmIds) {
        films.sort(Comparator.comparingInt(film -> popularFilmIds.indexOf(film.getId())));
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
        int updatedCount = jdbcTemplate.update(UpdateFilmSql.create(), film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (updatedCount == 0) {
            log.error("Failed to update film");
            throw new NotFoundException("Film with id " + film.getId() + " not found");
        }
        return film;
    }
}