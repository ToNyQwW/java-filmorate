package ru.yandex.practicum.filmorate.dao.friendship;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.friendship.sql.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@AllArgsConstructor
public class FriendshipDaoImpl implements FriendshipDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void addFriend(Long userId, Long friendId) {
        try {
            jdbcTemplate.update(AddFriendshipSql.create(), userId, friendId);
        } catch (DataAccessException e) {
            log.error("Can't add friendship", e);
            throw new NotFoundException("Cannot create friendship: user(s) not found");
        }
    }

    @Override
    public List<Long> getFriends(Long userId) {
        return jdbcTemplate.queryForList(GetFriendsByIdSql.create(), Long.class, userId);
    }

    @Override
    public Map<Long, List<Long>> getFriendsByUserIds(List<Long> userIds) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userIds", userIds);

        return namedParameterJdbcTemplate.query(GetFriendsByListIdsSql.create(), params, rs -> {
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
        return jdbcTemplate.queryForList(GetCommonFriendsSql.create(), Long.class, id, otherId);
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        jdbcTemplate.update(RemoveFriendshipSql.create(), userId, friendId);
    }
}