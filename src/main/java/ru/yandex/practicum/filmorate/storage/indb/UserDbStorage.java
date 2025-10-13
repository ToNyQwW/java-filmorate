package ru.yandex.practicum.filmorate.storage.indb;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserDbStorage implements UserStorage {


    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean containsUserId(Long id) {
        return false;
    }

    @Override
    public User createUser(User user) {
        normalize(user);

        String sql = """
                INSERT INTO users
                    (email, login, name, birthday)
                VALUES (?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        user.setId(keyHolder.getKeyAs(Long.class));
        return user;
    }

    @Override
    public Optional<User> getUser(Long id) {
        return Optional.empty();
    }

    @Override
    public List<User> getUsers() {
        return List.of();
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void addFriend(Long id, Long friendId) {

    }

    @Override
    public void removeFriend(Long id, Long friendId) {

    }

    @Override
    public List<User> getFriends(Long id) {
        return List.of();
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        return List.of();
    }

    private void normalize(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
