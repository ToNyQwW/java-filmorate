package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users;

    private Long id;

    public InMemoryUserStorage() {
        this.users = new HashMap<>();
        id = 0L;
    }

    @Override
    public boolean containsUserId(Long id) {
        return users.containsKey(id);
    }

    @Override
    public User createUser(User user) {
        user.setId(++id);
        normalize(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user) {
        var userId = user.getId();
        if (!users.containsKey(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        normalize(user);
        users.put(userId, user);
        return user;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        users.get(id).addFriend(friendId);
        users.get(friendId).addFriend(id);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        users.get(id).removeFriend(friendId);
        users.get(friendId).removeFriend(id);
    }

    @Override
    public List<User> getFriends(Long id) {
        return users.get(id).getFriends().stream()
                .map(users::get)
                .toList();
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        Set<Long> otherFriendsSet = new HashSet<>(users.get(otherId).getFriends());

        return users.get(id).getFriends().stream()
                .filter(otherFriendsSet::contains)
                .map(users::get)
                .toList();
    }

    private void normalize(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}