package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserStorageTest {

    private UserStorage userStorage;

    @BeforeEach
    void setUp() {
        userStorage = new InMemoryUserStorage();
    }

    private User createUser(String login) {
        User user = new User();
        user.setLogin(login);
        user.setEmail(login + "@mail.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        return user;
    }

    @Test
    void createUser() {
        User user = createUser("login123");
        User saved = userStorage.createUser(user);

        assertNotNull(saved.getId());
        assertEquals("login123", saved.getName(), "Имя пустое - должен быть использован логин");
        assertTrue(userStorage.containsUserId(saved.getId()));
    }

    @Test
    void updateUser() {
        User user = createUser("login");
        User saved = userStorage.createUser(user);

        saved.setName("UpdatedName");
        User updated = userStorage.updateUser(saved);

        assertEquals("UpdatedName", userStorage.getUser(saved.getId()).get().getName());
        assertEquals(updated, userStorage.getUser(saved.getId()).get());
    }

    @Test
    void addFriend() {
        User u1 = userStorage.createUser(createUser("u1"));
        User u2 = userStorage.createUser(createUser("u2"));

        userStorage.addFriend(u1.getId(), u2.getId());

        List<User> friendsOfU1 = userStorage.getFriends(u1.getId());
        List<User> friendsOfU2 = userStorage.getFriends(u2.getId());

        assertTrue(friendsOfU1.contains(u2));
        assertTrue(friendsOfU2.contains(u1));
    }

    @Test
    void getCommonFriends() {
        User u1 = userStorage.createUser(createUser("u1"));
        User u2 = userStorage.createUser(createUser("u2"));
        User u3 = userStorage.createUser(createUser("u3"));

        userStorage.addFriend(u1.getId(), u3.getId());
        userStorage.addFriend(u2.getId(), u3.getId());

        List<User> common = userStorage.getCommonFriends(u1.getId(), u2.getId());

        assertEquals(List.of(u3), common);
    }
}