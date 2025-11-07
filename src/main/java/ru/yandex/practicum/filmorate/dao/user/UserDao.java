package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User createUser(User user);

    Optional<User> getUser(Long id);

    List<User> getUsersByIds(List<Long> ids);

    List<User> getAllUsers();

    User updateUser(User user);
}