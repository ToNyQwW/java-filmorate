package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.friendship.FriendshipDao;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

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
    public User getUser(Long id) throws NotFoundException {
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
    public User updateUser(User user) throws NotFoundException {
        try {
            normalize(user);
            var updatedUser = userDao.updateUser(user);
            log.info("User updated: {}", updatedUser);
            return updatedUser;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void addFriend(Long id, Long friendId) throws NotFoundException {
        try {
            friendshipDao.addFriend(id, friendId);
            log.info("Friend added: {}", friendId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeFriend(Long id, Long friendId) throws NotFoundException {
        try {
            friendshipDao.removeFriend(id, friendId);
            log.info("Friend removed: {}", friendId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<User> getFriends(Long id) throws NotFoundException {
        try {
            var friends = friendshipDao.getFriends(id);
            log.info("Number of friends found: {}", friends.getFriendsId().size());
            return userDao.getUsersByIds(friends.getFriendsId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) throws NotFoundException {
        try {
            var commonFriends = friendshipDao.getCommonFriends(id, otherId);
            log.info("Number of common friends found: {}", commonFriends.size());
            return userDao.getUsersByIds(commonFriends);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private void normalize(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}