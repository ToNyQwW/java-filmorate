package ru.yandex.practicum.filmorate.dao.likes;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.entity.Likes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LikesDao {

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
                    WHERE film_id IN (:filmIds)
            """;

    private static final String REMOVE_LIKE_SQL = """
                    DELETE FROM likes
                    WHERE film_id = ? AND user_id = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void addLike(Long filmId, Long userId) {
        jdbcTemplate.update(ADD_LIKE_SQL, filmId, userId);
    }

    public Likes getFilmLikes(Long filmId) {
        List<Long> userIds = jdbcTemplate.query(GET_LIKES_BY_FILM_ID_SQL,
                (rs, rowNum) -> rs.getLong("user_id"),
                filmId);
        return new Likes(filmId, userIds);
    }

    public Map<Long, Likes> getUserLikesByFilmIds(List<Long> filmsIds) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmsIds", filmsIds);

        return namedParameterJdbcTemplate.query(GET_USER_IDS_BY_FILM_IDS_SQL, params, rs -> {
            Map<Long, Likes> result = new HashMap<>();

            while (rs.next()) {
                var filmId = rs.getLong("film_id");
                var userId = rs.getLong("user_id");

                result.computeIfAbsent(filmId, value -> new Likes(filmId, new ArrayList<>()))
                        .getUsersId().add(userId);
            }

            return result;
        });
    }

    public List<Long> getPopularFilmIds(Long count) {
        return jdbcTemplate.queryForList(GET_POPULAR_FILMS_ID_SQL, Long.class, count);
    }

    public void removeLike(Long filmId, Long userId) {
        jdbcTemplate.update(REMOVE_LIKE_SQL, filmId, userId);
    }
}