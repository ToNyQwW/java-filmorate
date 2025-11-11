package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.friendship.FriendshipDao;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.entity.Event;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.service.events.EventsService;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final EventsService eventsService;
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
            var friends = friendshipDao.getFriends(id);
            findedUser.setFriends(friends);
            log.info("User found: {}", findedUser);
            return findedUser;
        }
        log.error("User with id {} not found", id);
        throw new NotFoundException("User with id " + id + " not found");
    }

    @Override
    public List<User> getUsers() {
        var users = userDao.getAllUsers();
        buildUsers(users);
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

        eventsService.createEvent(Event.builder()
                .entityId(friendId)
                .timestamp(Instant.now().toEpochMilli())
                .eventType(EventType.FRIEND)
                .operation(EventOperation.ADD)
                .userId(id)
                .build());
    }

    @Override
    public void removeFriend(Long id, Long friendId) {
        throwIfUserIdNotExists(id);
        throwIfUserIdNotExists(friendId);
        friendshipDao.removeFriend(id, friendId);
        log.info("Friend removed: {}", friendId);

        eventsService.createEvent(Event.builder()
                .entityId(friendId)
                .timestamp(Instant.now().toEpochMilli())
                .eventType(EventType.FRIEND)
                .operation(EventOperation.REMOVE)
                .userId(id)
                .build());
    }

    @Override
    public List<User> getFriends(Long id) {
        throwIfUserIdNotExists(id);
        var friendsIds = friendshipDao.getFriends(id);
        var friends = userDao.getUsersByIds(friendsIds);
        buildUsers(friends);
        log.info("Number of friends found: {}", friends.size());
        return friends;
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
            log.error("User with id {} not found", userId);
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }

    private void normalize(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void buildUsers(List<User> users) {
        var userIds = users.stream()
                .map(User::getId).toList();

        var friendsByUserIds = friendshipDao.getFriendsByUserIds(userIds);

        for (User user : users) {
            var friendship = friendsByUserIds.get(user.getId());
            user.setFriends(friendship);
        }
    }
}