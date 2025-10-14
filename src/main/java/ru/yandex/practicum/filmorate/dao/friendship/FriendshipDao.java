package ru.yandex.practicum.filmorate.dao.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.entity.Friendship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendshipDao {

    private static final String ADD_FRIENDSHIP_SQL = """
            INSERT INTO friendship
                (user_id, friend_id, is_confirmed)
            VALUES (?, ?, ?)
            """;

    private static final String GET_FRIENDS_BY_ID_SQL = """
            SELECT friend_id
            FROM friendship
            WHERE user_id = ? AND is_confirmed = true;
            """;

    private static final String GET_FRIENDS_BY_LIST_IDS_SQL = """
            SELECT user_id,
                 friend_id
            FROM friendship
            WHERE user_id IN (:userIds) AND is_confirmed = true;
            """;

    private static final String REMOVE_FRIENDSHIP_SQL = """
            DELETE FROM friendship
            WHERE (user_id = ? AND friend_id = ?)
            OR (user_id = ? AND friend_id = ?);
            """;

    private static final String GET_COMMON_FRIENDS_SQL = """
            SELECT f1.friend_id
            FROM friendship f1
            INNER JOIN friendship f2 ON f1.friend_id = f2.friend_id
            WHERE f1.user_id = ? AND f2.user_id = ?;
            """;

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void addFriendship(Long userId, Long friendId) {
        jdbcTemplate.update(ADD_FRIENDSHIP_SQL, userId, friendId, true);
    }

    public Friendship getFriends(Long userId) {
        List<Long> friendsId = jdbcTemplate.queryForList(GET_FRIENDS_BY_ID_SQL, Long.class, userId);
        return new Friendship(userId, friendsId);
    }

    public Map<Long, Friendship> getFriendsByUserIds(List<Long> userIds) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userIds", userIds);

        return namedParameterJdbcTemplate.query(GET_FRIENDS_BY_LIST_IDS_SQL, params, rs -> {
            Map<Long, Friendship> result = new HashMap<>();

            while (rs.next()) {
                Long userId = rs.getLong("user_id");
                Long friendId = rs.getLong("friend_id");

                result.computeIfAbsent(userId, value -> new Friendship(userId, new ArrayList<>()))
                        .getFriendsId().add(friendId);
            }

            return result;
        });
    }

    public List<Long> getCommonFriends(Long id, Long otherId) {
        return jdbcTemplate.queryForList(GET_COMMON_FRIENDS_SQL, Long.class, id, otherId);
    }

    public void removeFriendship(Long userId, Long friendId) {
        jdbcTemplate.update(REMOVE_FRIENDSHIP_SQL, userId, friendId, friendId, userId);
    }
}
