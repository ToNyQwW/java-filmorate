package ru.yandex.practicum.filmorate.dao.friendship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FriendshipDaoImplTest {

    @Autowired
    private FriendshipDao friendshipDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update(
                "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)",
                "alice@mail.com", "alice", "Alice", LocalDate.of(1990, 1, 1)
        );
        jdbcTemplate.update(
                "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)",
                "bob@mail.com", "bob", "Bob", LocalDate.of(1991, 2, 2)
        );
        jdbcTemplate.update(
                "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)",
                "charlie@mail.com", "charlie", "Charlie", LocalDate.of(1992, 3, 3)
        );
        jdbcTemplate.update(
                "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)",
                "dave@mail.com", "dave", "Dave", LocalDate.of(1993, 4, 4)
        );
    }

    @Test
    @DisplayName("Добавление и получение друзей пользователя")
    void addAndGetFriends() {
        friendshipDao.addFriend(1L, 2L);
        friendshipDao.addFriend(1L, 3L);

        List<Long> friends = friendshipDao.getFriends(1L);

        assertNotNull(friends);
        assertEquals(2, friends.size());
        assertTrue(friends.contains(2L));
        assertTrue(friends.contains(3L));
    }

    @Test
    @DisplayName("Удаление друга")
    void removeFriend() {
        friendshipDao.addFriend(1L, 2L);
        friendshipDao.addFriend(1L, 3L);

        friendshipDao.removeFriend(1L, 2L);

        List<Long> friends = friendshipDao.getFriends(1L);

        assertEquals(1, friends.size());
        assertEquals(3L, friends.getFirst());
    }

    @Test
    @DisplayName("Получение общих друзей")
    void getCommonFriends() {
        friendshipDao.addFriend(1L, 3L);
        friendshipDao.addFriend(2L, 3L);
        friendshipDao.addFriend(2L, 4L);

        List<Long> commonFriends = friendshipDao.getCommonFriends(1L, 2L);

        assertNotNull(commonFriends);
        assertEquals(1, commonFriends.size());
        assertEquals(3L, commonFriends.getFirst());
    }

    @Test
    @DisplayName("Получение друзей по списку пользователей")
    void getFriendsByUserIds() {
        friendshipDao.addFriend(1L, 2L);
        friendshipDao.addFriend(1L, 3L);
        friendshipDao.addFriend(2L, 3L);

        Map<Long, List<Long>> result = friendshipDao.getFriendsByUserIds(List.of(1L, 2L));

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey(1L));
        assertTrue(result.containsKey(2L));

        List<Long> friends1 = result.get(1L);
        List<Long> friends2 = result.get(2L);

        assertEquals(2, friends1.size());
        assertTrue(friends1.contains(2L));
        assertTrue(friends1.contains(3L));

        assertEquals(1, friends2.size());
        assertTrue(friends2.contains(3L));
    }
}