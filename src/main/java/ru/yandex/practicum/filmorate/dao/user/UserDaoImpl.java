package ru.yandex.practicum.filmorate.dao.user;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.friendship.FriendshipDao;
import ru.yandex.practicum.filmorate.entity.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserDaoImpl implements UserDao {

    private static final String CREATE_USER_SQL = """
            INSERT INTO users
                (email, login, name, birthday)
            VALUES (?, ?, ?, ?)
            """;

    private static final String GET_USER_SQL = """
            SELECT user_id AS id,
                   email,
                   login,
                   name,
                   birthday
            FROM users
            WHERE user_id = ?
            """;

    private static final String GET_USERS_BY_LIST_IDS_SQL = """
            SELECT user_id AS id,
                   email,
                   login,
                   name,
                   birthday
            FROM users
            WHERE user_id IN (:userIds)
            """;

    private static final String GET_ALL_USERS_SQL = """
            SELECT user_id AS id,
                   email,
                   login,
                   name,
                   birthday
            FROM users
            """;

    private static final String UPDATE_USER_SQL = """
            UPDATE users
            SET email = ?,
                login = ?,
                name = ?,
                birthday = ?
            WHERE user_id = ?
            """;

    private static final RowMapper<User> USER_ROW_MAPPER = new BeanPropertyRowMapper<>(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final FriendshipDao friendshipDao;

    @Override
    public User createUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_USER_SQL, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            user.setId(keyHolder.getKey().longValue());
        }

        return user;
    }

    @Override
    public Optional<User> getUser(Long id) {
        var user = jdbcTemplate.queryForObject(GET_USER_SQL, USER_ROW_MAPPER, id);

        var friends = friendshipDao.getFriends(id);

        if (user != null) {
            user.setFriends(friends);
        }

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getUsersByIds(List<Long> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userIds", ids);

        var users = namedParameterJdbcTemplate.query(GET_USERS_BY_LIST_IDS_SQL, params, USER_ROW_MAPPER);

        return buildUsers(users);
    }

    @Override
    public List<User> getAllUsers() {
        var users = jdbcTemplate.query(GET_ALL_USERS_SQL, USER_ROW_MAPPER);

        return buildUsers(users);
    }

    private List<User> buildUsers(List<User> users) {
        var userIds = users.stream()
                .map(User::getId).toList();

        var friendsByUserIds = friendshipDao.getFriendsByUserIds(userIds);

        for (User user : users) {
            var friendship = friendsByUserIds.get(user.getId());
            user.setFriends(friendship);
        }

        return users;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(UPDATE_USER_SQL,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        return user;
    }
}
