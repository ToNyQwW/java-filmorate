package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User createUser(User user) {
        var createdUser = userStorage.createUser(user);
        log.info("User created: {}", createdUser);
        return createdUser;
    }

    @Override
    public User getUser(Long id) throws NotFoundException {
        var user = userStorage.getUser(id);
        if (user.isPresent()) {
            var findedUser = user.get();
            log.info("User found: {}", findedUser);
            return findedUser;
        }
        log.info("User with id {} not found", id);
        throw new NotFoundException("User with id " + id + " not found");
    }

    @Override
    public List<User> getUsers() {
        var users = userStorage.getUsers();
        log.info("Number of users found: {}", users);
        return users;
    }

    @Override
    public User updateUser(User user) throws NotFoundException {
        try {
            var updatedUser = userStorage.updateUser(user);
            log.info("User updated: {}", updatedUser);
            return updatedUser;
        } catch (NotFoundException e) {
            log.info(e.getMessage());
            throw e;
        }
    }

    @Override
    public void addFriend(Long id, Long friendId) throws NotFoundException {
        try {
            userStorage.addFriend(id, friendId);
            log.info("Friend added: {}", friendId);
        } catch (NotFoundException e) {
            log.info(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeFriend(Long id, Long friendId) throws NotFoundException {
        try {
            userStorage.removeFriend(id, friendId);
            log.info("Friend removed: {}", friendId);
        } catch (NotFoundException e) {
            log.info(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<User> getFriends(Long id) throws NotFoundException {
        try {
            var friends = userStorage.getFriends(id);
            log.info("Number of friends found: {}", friends);
            return friends;
        } catch (NotFoundException e) {
            log.info(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) throws NotFoundException {
        try {
            var commonFriends = userStorage.getCommonFriends(id, otherId);
            log.info("Number of common friends found: {}", commonFriends);
            return commonFriends;
        } catch (NotFoundException e) {
            log.info(e.getMessage());
            throw e;
        }
    }
}