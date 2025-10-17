package ru.yandex.practicum.filmorate.dao.user;

import lombok.AllArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.friendship.FriendshipDao;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserDaoImpl implements UserDao {

    private static final String CREATE_USER_SQL = """
            INSERT INTO users
                (email, login, name, birthday)
            VALUES (:email, :login, :name, :birthday)
            """;

    private static final String GET_USER_SQL = """
            SELECT user_id,
                   email,
                   login,
                   name,
                   birthday
            FROM users
            WHERE user_id = ?
            """;

    private static final String GET_USERS_BY_LIST_IDS_SQL = """
            SELECT user_id,
                   email,
                   login,
                   name,
                   birthday
            FROM users
            WHERE user_id IN (:userIds)
            """;

    private static final String GET_ALL_USERS_SQL = """
            SELECT user_id,
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

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;
    private final FriendshipDao friendshipDao;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public User createUser(User user) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(CREATE_USER_SQL, params, keyHolder);

        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public Optional<User> getUser(Long id) {
        var users = jdbcTemplate.query(GET_USER_SQL, userRowMapper, id);
        User user = DataAccessUtils.singleResult(users);

        if (user != null) {
            var friends = friendshipDao.getFriends(id);
            user.setFriends(friends);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getUsersByIds(List<Long> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userIds", ids);

        var users = namedParameterJdbcTemplate.query(GET_USERS_BY_LIST_IDS_SQL, params, userRowMapper);

        return buildUsers(users);
    }

    @Override
    public List<User> getAllUsers() {
        var users = jdbcTemplate.query(GET_ALL_USERS_SQL, userRowMapper);

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
        var id = user.getId();
        var update = jdbcTemplate.update(UPDATE_USER_SQL,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                id);

        if (update == 0) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        return user;
    }
}