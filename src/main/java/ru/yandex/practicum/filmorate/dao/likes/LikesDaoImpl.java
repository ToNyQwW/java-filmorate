package ru.yandex.practicum.filmorate.dao.likes;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.likes.sql.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@AllArgsConstructor
public class LikesDaoImpl implements LikesDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void addLike(Long filmId, Long userId) {
        try {
            jdbcTemplate.update(AddLikeSql.create(), filmId, userId);
        } catch (DataAccessException e) {
            log.error("Error adding like to DB", e);
            throw new NotFoundException("Cannot add like: user(s) not found");
        }
    }

    @Override
    public List<Long> getFilmLikes(Long filmId) {
        return jdbcTemplate.queryForList(GetLikesByFilmIdSql.create(), Long.class, filmId);
    }

    @Override
    public Map<Long, List<Long>> getUserLikesByFilmIds(List<Long> filmsIds) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmsIds", filmsIds);

        return namedParameterJdbcTemplate.query(GetUserIdsByFilmIdsSql.create(), params, rs -> {
            Map<Long, List<Long>> result = new HashMap<>();
            for (Long filmsId : filmsIds) {
                result.put(filmsId, new ArrayList<>());
            }

            while (rs.next()) {
                var filmId = rs.getLong("film_id");
                var userId = rs.getLong("user_id");

                result.get(filmId).add(userId);
            }

            return result;
        });
    }

    @Override
    public List<Long> getPopularFilmIds(Long count) {
        return jdbcTemplate.queryForList(GetPopularFilmsIdSql.create(), Long.class, count);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        var updatedCount = jdbcTemplate.update(RemoveLikeSql.create(), filmId, userId);

        if (updatedCount == 0) {
            throw new NotFoundException("Cannot remove like: user or film not found");
        }
    }

    @Override
    public List<Long> getRecommendationsFilmIds(Long userId) {
        var similarUserId = jdbcTemplate.queryForList(
                GetMostCommonUserByLikesSql.create(),
                Long.class, userId, userId).getFirst();

        return jdbcTemplate.queryForList(GetRecommendedFilmsBySimilarUserSql.create(),
                Long.class, similarUserId, userId);
    }
}