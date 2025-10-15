package ru.yandex.practicum.filmorate.dao.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendshipDaoImpl implements FriendshipDao {

    private static final String ADD_FRIENDSHIP_SQL = """
            INSERT INTO friendship
                (user_id, friend_id)
            VALUES (?, ?)
            """;

    private static final String GET_FRIENDS_BY_ID_SQL = """
            SELECT friend_id
            FROM friendship
            WHERE user_id = ?
            """;

    private static final String GET_FRIENDS_BY_LIST_IDS_SQL = """
            SELECT user_id,
                 friend_id
            FROM friendship
            WHERE user_id IN (:userIds)
            """;

    private static final String REMOVE_FRIENDSHIP_SQL = """
            DELETE FROM friendship
            WHERE user_id = ? AND friend_id = ?
            """;

    private static final String GET_COMMON_FRIENDS_SQL = """
            SELECT f1.friend_id
            FROM friendship f1
            INNER JOIN friendship f2 ON f1.friend_id = f2.friend_id
            WHERE f1.user_id = ? AND f2.user_id = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void addFriend(Long userId, Long friendId) {
        jdbcTemplate.update(ADD_FRIENDSHIP_SQL, userId, friendId);
    }

    @Override
    public List<Long> getFriends(Long userId) {
        return jdbcTemplate.queryForList(GET_FRIENDS_BY_ID_SQL, Long.class, userId);
    }

    @Override
    public Map<Long, List<Long>> getFriendsByUserIds(List<Long> userIds) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userIds", userIds);

        return namedParameterJdbcTemplate.query(GET_FRIENDS_BY_LIST_IDS_SQL, params, rs -> {
            Map<Long, List<Long>> result = new HashMap<>();

            while (rs.next()) {
                Long userId = rs.getLong("user_id");
                Long friendId = rs.getLong("friend_id");

                result.computeIfAbsent(userId, value -> new ArrayList<>()).add(friendId);
            }
            return result;
        });
    }

    @Override
    public List<Long> getCommonFriends(Long id, Long otherId) {
        return jdbcTemplate.queryForList(GET_COMMON_FRIENDS_SQL, Long.class, id, otherId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        jdbcTemplate.update(REMOVE_FRIENDSHIP_SQL, userId, friendId);
    }
}
