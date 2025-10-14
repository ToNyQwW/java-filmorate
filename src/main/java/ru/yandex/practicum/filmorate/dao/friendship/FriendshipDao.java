package ru.yandex.practicum.filmorate.dao.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.entity.Friendship;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FriendshipDao {

    private static final String ADD_FRIENDSHIP_SQL = """
            INSERT INTO friendship
                (user_id, friend_id, is_confirmed)
            VALUES (?, ?, ?)
            """;

    private static final String GET_FRIENDS_SQL = """
            SELECT friend_id
            FROM friendship
            WHERE user_id = ? AND is_confirmed = true;
            """;

    private static final String CONFIRM_FRIENDSHIP_SQL = """
            UPDATE friendship
            SET is_confirmed = true
            WHERE (user_id = ? AND friend_id = ?)
               OR (user_id = ? AND friend_id = ?);
            """;

    private static final String REMOVE_FRIENDSHIP_SQL = """
            DELETE FROM friendship
            WHERE (user_id = ? AND friend_id = ?)
            OR (user_id = ? AND friend_id = ?);
            """;

    private final JdbcTemplate jdbcTemplate;

    public void addFriendship(Friendship friendship) {
        jdbcTemplate.update(ADD_FRIENDSHIP_SQL, friendship.getUserId(), friendship.getFriendId(), true);
    }

    public List<Long> getFriends(Long userId) {
        return jdbcTemplate.queryForList(GET_FRIENDS_SQL, Long.class, userId);
    }

    public void confirmFriendship(Long userId, Long friendId) {
        jdbcTemplate.update(CONFIRM_FRIENDSHIP_SQL, userId, friendId, friendId, userId);
    }

    public void removeFriendship(Long userId, Long friendId) {
        jdbcTemplate.update(REMOVE_FRIENDSHIP_SQL, userId, friendId, friendId, userId);
    }
}
