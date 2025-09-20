package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users;

    private int id;

    public InMemoryUserStorage() {
        this.users = new HashMap<>();
    }

    @Override
    public User createUser(User user) {
        user.setId(++id);
        normalize(user);
        users.put(user.getId(), user);
        log.info("User created: {}", user);
        return user;
    }

    @Override
    public User getUser(int id) {
        return users.get(id);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user) {
        int userId = user.getId();
        if (!users.containsKey(userId)) {
            log.warn("User not found: {}", userId);
            throw new NotFoundException("User not found");
        }
        normalize(user);
        users.put(user.getId(), user);
        log.info("User updated: {}", user);
        return user;
    }

    @Override
    public void addFriend(int id, int friendId) {
        users.get(id).addFriend(friendId);
        users.get(friendId).addFriend(id);
    }

    @Override
    public void removeFriend(int id, int friendId) {
        users.get(id).removeFriend(friendId);
    }

    @Override
    public List<User> getFriends(int id) {
        return users.get(id).getFriends().stream()
                .map(users::get)
                .toList();
    }

    private void normalize(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
