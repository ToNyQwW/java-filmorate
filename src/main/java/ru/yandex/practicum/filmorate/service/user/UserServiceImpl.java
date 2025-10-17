package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.friendship.FriendshipDao;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final FriendshipDao friendshipDao;

    @Override
    public User createUser(User user) {
        normalize(user);
        var createdUser = userDao.createUser(user);
        log.info("User created: {}", createdUser);
        return createdUser;
    }

    @Override
    public User getUser(Long id) {
        var user = userDao.getUser(id);
        if (user.isPresent()) {
            var findedUser = user.get();
            log.info("User found: {}", findedUser);
            return findedUser;
        }
        log.error("User with id {} not found", id);
        throw new NotFoundException("User with id " + id + " not found");
    }

    @Override
    public List<User> getUsers() {
        var users = userDao.getAllUsers();
        log.info("Number of users found: {}", users.size());
        return users;
    }

    @Override
    public User updateUser(User user) {
        normalize(user);
        var updatedUser = userDao.updateUser(user);
        log.info("User updated: {}", updatedUser);
        return updatedUser;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        friendshipDao.addFriend(id, friendId);
        log.info("Friend added: {}", friendId);
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        throwIfUserIdNotExists(id);
        throwIfUserIdNotExists(friendId);
        friendshipDao.removeFriend(id, friendId);
        log.info("Friend removed: {}", friendId);
    }

    @Override
    public List<User> getFriends(Long id) {
        throwIfUserIdNotExists(id);
        var friends = friendshipDao.getFriends(id);
        log.info("Number of friends found: {}", friends.size());
        return userDao.getUsersByIds(friends);
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        throwIfUserIdNotExists(id);
        throwIfUserIdNotExists(otherId);
        var commonFriends = friendshipDao.getCommonFriends(id, otherId);
        log.info("Number of common friends found: {}", commonFriends.size());

        if (commonFriends.isEmpty()) {
            return Collections.emptyList();
        }
        return userDao.getUsersByIds(commonFriends);
    }

    private void throwIfUserIdNotExists(Long userId) {
        var user = userDao.getUser(userId);

        if (user.isEmpty()) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }

    private void normalize(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}