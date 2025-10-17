package ru.yandex.practicum.filmorate.dao.likes;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
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
                    SELECT film_id
                    FROM likes
                    GROUP BY film_id
                    ORDER BY COUNT(user_id) DESC
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
        return jdbcTemplate.queryForList(GET_LIKES_BY_FILM_ID_SQL, Long.class, filmId);
    }

    @Override
    public Map<Long, List<Long>> getUserLikesByFilmIds(List<Long> filmsIds) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmsIds", filmsIds);

        return namedParameterJdbcTemplate.query(GET_USER_IDS_BY_FILM_IDS_SQL, params, rs -> {
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
        return jdbcTemplate.queryForList(GET_POPULAR_FILMS_ID_SQL, Long.class, count);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        jdbcTemplate.update(REMOVE_LIKE_SQL, filmId, userId);
    }
}