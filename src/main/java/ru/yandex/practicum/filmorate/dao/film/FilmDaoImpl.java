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
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class FilmDaoImpl implements FilmDao {

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

        return Optional.ofNullable(film);
    }

    @Override
    public List<Film> getFilms() {
        return jdbcTemplate.query(GetAllFilmsWithMpaSql.create(), filmRowMapper);
    }

    @Override
    public List<Film> getFilmsByListIds(List<Long> filmIds) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmsIds", filmIds);

        return namedParameterJdbcTemplate.query(GetFilmsByListIdsSql.create(), params, filmRowMapper);
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