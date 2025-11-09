package ru.yandex.practicum.filmorate.dao.directors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.directors.sql.*;
import ru.yandex.practicum.filmorate.entity.Director;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class DirectorsDaoImpl implements DirectorsDao {

    private final JdbcTemplate jdbcTemplate;
    private final DirectorRowMapper directorRowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Director addDirector(Director director) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", director.getName());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(AddDirectorSql.create(), params, keyHolder);

        director.setId(keyHolder.getKey().longValue());

        return director;
    }

    @Override
    public List<Director> getAllDirectors() {
        return jdbcTemplate.query(GetAllDirectorsSql.create(), directorRowMapper);
    }

    @Override
    public List<Director> getDirectorsByListIds(List<Long> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("directorIds", ids);

        return namedParameterJdbcTemplate.query(GetDirectorsByListIds.create(), params, directorRowMapper);
    }

    @Override
    public Optional<Director> getDirectorById(Long id) {
        var result = jdbcTemplate.query(GetDirectorByIdSql.create(), directorRowMapper, id);
        var review = DataAccessUtils.singleResult(result);

        return Optional.ofNullable(review);
    }

    @Override
    public Director updateDirector(Director director) {
        int updatedCount = jdbcTemplate.update(UpdateDirectorSql.create(), director.getName(), director.getId());

        if (updatedCount == 0) {
            log.error("Failed to update director");
            throw new NotFoundException("director with id " + director.getId() + " not found");
        }
        return director;
    }

    @Override
    public void deleteDirectorById(Long id) {
        var updatedCount = jdbcTemplate.update(DeleteDirectorSql.create(), id);

        if (updatedCount == 0) {
            log.error("Failed to delete director");
            throw new NotFoundException("director with id " + id + " not found");
        }
    }
}