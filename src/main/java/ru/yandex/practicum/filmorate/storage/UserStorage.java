package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    boolean containsUserId(Long id);

    User createUser(User user);

    Optional<User> getUser(Long id);

    List<User> getUsers();

    User updateUser(User user);

    void addFriend(Long id, Long friendId);

    void removeFriend(Long id, Long friendId);

    List<User> getFriends(Long id);

    List<User> getCommonFriends(Long id, Long otherId);
}
