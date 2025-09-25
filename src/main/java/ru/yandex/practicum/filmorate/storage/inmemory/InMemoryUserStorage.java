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

    private void throwIfUserIdNotExists(Long userId) throws NotFoundException {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }

    @Override
    public User updateUser(User user) throws NotFoundException {
        var userId = user.getId();
        throwIfUserIdNotExists(userId);

        normalize(user);
        users.put(userId, user);
        return user;
    }

    @Override
    public void addFriend(Long id, Long friendId) throws NotFoundException {
        var user = users.get(id);
        var friend = users.get(friendId);

        if (user == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        if (friend == null) {
            throw new NotFoundException("User with id " + friendId + " not found");
        }

        user.addFriend(friendId);
        friend.addFriend(id);
    }

    @Override
    public void removeFriend(Long id, Long friendId) throws NotFoundException {
        var user = users.get(id);
        var friend = users.get(friendId);

        if (user == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        if (friend == null) {
            throw new NotFoundException("User with id " + friendId + " not found");
        }

        user.removeFriend(friendId);
        friend.removeFriend(id);
    }

    @Override
    public List<User> getFriends(Long id) throws NotFoundException {
        var user = users.get(id);

        if (user == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }

        return user.getFriends().stream()
                .map(users::get)
                .toList();
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) throws NotFoundException {
        var user = users.get(id);
        var otherUser = users.get(otherId);

        if (user == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        if (otherUser == null) {
            throw new NotFoundException("User with id " + otherId + " not found");
        }

        Set<Long> otherFriendsSet = new HashSet<>(otherUser.getFriends());

        return user.getFriends().stream()
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