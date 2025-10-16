package ru.yandex.practicum.filmorate.dao.likes;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikesDaoImpl implements LikesDao {

    private static final String ADD_LIKE_SQL = """
                    INSERT INTO likes
                        (film_id, user_id)
                    VALUES (?, ?)
            """;


    private static final String GET_LIKES_BY_FILM_ID_SQL = """
                    SELECT user_id
                    FROM likes
                    WHERE film_id = ?
            """;
    private static final String GET_POPULAR_FILMS_ID_SQL = """
                    SELECT film_id AS filmId,
                        COUNT(user_id) AS likeCount
                    FROM likes
                    GROUP BY film_id
                    ORDER BY likeCount DESC
                    LIMIT ?
            """;

    private static final String GET_USER_IDS_BY_FILM_IDS_SQL = """
                    SELECT film_id,
                           user_id
                    FROM likes
                    WHERE film_id IN (:filmsIds)
            """;

    private static final String REMOVE_LIKE_SQL = """
                    DELETE FROM likes
                    WHERE film_id = ? AND user_id = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void addLike(Long filmId, Long userId) {
        jdbcTemplate.update(ADD_LIKE_SQL, filmId, userId);
    }

    @Override
    public List<Long> getFilmLikes(Long filmId) {
        List<Long> userIds = jdbcTemplate.query(GET_LIKES_BY_FILM_ID_SQL,
                (rs, rowNum) -> rs.getLong("user_id"),
                filmId);
        return new ArrayList<>(userIds);
    }

    @Override
    public Map<Long, List<Long>> getUserLikesByFilmIds(List<Long> filmsIds) {
        if (filmsIds == null || filmsIds.isEmpty()) {
            return Collections.emptyMap();
        }

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmsIds", filmsIds);

        return namedParameterJdbcTemplate.query(GET_USER_IDS_BY_FILM_IDS_SQL, params, rs -> {
            Map<Long, List<Long>> result = new HashMap<>();

            while (rs.next()) {
                var filmId = rs.getLong("film_id");
                var userId = rs.getLong("user_id");

                result.computeIfAbsent(filmId, value -> new ArrayList<>()).add(userId);
            }

            //TODO ??? надо ли
            for (Long id : filmsIds) {
                result.putIfAbsent(id, new ArrayList<>());
            }

            return result;
        });
    }

    @Override
    public List<Long> getPopularFilmIds(Long count) {
        return jdbcTemplate.query(GET_POPULAR_FILMS_ID_SQL, rs -> {
            List<Long> result = new ArrayList<>();

            while (rs.next()) {
                var filmId = rs.getLong("filmId");
                result.add(filmId);
            }
            return result;
        }, count);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        jdbcTemplate.update(REMOVE_LIKE_SQL, filmId, userId);
    }
}