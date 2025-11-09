package ru.yandex.practicum.filmorate.dao.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.user.sql.*;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@AllArgsConstructor
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User createUser(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(CreateUserSql.create(), params, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public Optional<User> getUser(Long id) {
        var users = jdbcTemplate.query(GetUserSql.create(), userRowMapper, id);
        User user = DataAccessUtils.singleResult(users);

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getUsersByIds(List<Long> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userIds", ids);

        return namedParameterJdbcTemplate.query(GetUsersByListIdsSql.create(), params, userRowMapper);
    }

    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(GetAllUsersSql.create(), userRowMapper);
    }

    @Override
    public User updateUser(User user) {
        var id = user.getId();
        var updatedCount = jdbcTemplate.update(UpdateUserSql.create(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                id);

        if (updatedCount == 0) {
            log.error("Error updating user");
            throw new NotFoundException("User with id " + id + " not found");
        }
        return user;
    }
}