package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.validUser.ValidUser;
import ru.yandex.practicum.filmorate.validation.validUserId.ValidUserId;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User getUser(@ValidUserId Long id);

    List<User> getUsers();

    User updateUser(@ValidUser User user);

    void addFriend(@ValidUserId Long id, @ValidUserId Long friendId);

    void removeFriend(@ValidUserId Long id, @ValidUserId Long friendId);

    List<User> getFriends(@ValidUserId Long id);

    List<User> getCommonFriends(@ValidUserId Long id, @ValidUserId Long otherId);
}
