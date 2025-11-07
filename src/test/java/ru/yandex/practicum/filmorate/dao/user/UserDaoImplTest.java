package ru.yandex.practicum.filmorate.dao.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDaoImplTest {

    @Autowired
    private UserDaoImpl userDao;

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
    }

    @Test
    @DisplayName("Создание нового пользователя — успешно сохраняется и доступен для получения по ID")
    void createUser() {
        User newUser = User.builder()
                .email("test@mail.com")
                .login("test")
                .name("Test")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        User saved = userDao.createUser(newUser);

        assertNotNull(saved.getId());

        Optional<User> fromDb = userDao.getUser(saved.getId());
        assertTrue(fromDb.isPresent());
        assertEquals("test@mail.com", fromDb.get().getEmail());
        assertEquals("test", fromDb.get().getLogin());
    }

    @Test
    @DisplayName("Получение пользователя по существующему ID — возвращается корректный объект")
    void getUser() {
        Optional<User> userOpt = userDao.getUser(1L);

        assertTrue(userOpt.isPresent());
        User user = userOpt.get();
        assertEquals("alice@mail.com", user.getEmail());
        assertEquals("alice", user.getLogin());
    }

    @Test
    @DisplayName("Получение всех пользователей — должен возвращаться полный список пользователей")
    void getAllUsers() {
        List<User> users = userDao.getAllUsers();

        assertNotNull(users);
        assertEquals(3, users.size());

        List<String> logins = users.stream().map(User::getLogin).toList();

        assertTrue(logins.containsAll(List.of("alice", "bob", "charlie")));
    }

    @Test
    @DisplayName("Получение пользователей по списку ID — возвращаются только выбранные пользователи")
    void getUsersByIds() {
        List<Long> ids = List.of(1L, 2L);

        List<User> users = userDao.getUsersByIds(ids);

        assertNotNull(users);
        assertEquals(2, users.size());

        List<String> logins = users.stream().map(User::getLogin).toList();
        assertTrue(logins.containsAll(List.of("alice", "bob")));
    }

    @Test
    @DisplayName("Обновление существующего пользователя — данные корректно изменяются в БД")
    void updateUser() {
        User existing = userDao.getUser(1L).orElseThrow();

        User updatedUser = User.builder()
                .id(existing.getId())
                .email("alice_new@mail.com")
                .login(existing.getLogin())
                .name("Alice Updated")
                .birthday(existing.getBirthday())
                .build();

        userDao.updateUser(updatedUser);

        User fromDb = userDao.getUser(1L).orElseThrow();
        assertEquals("Alice Updated", fromDb.getName());
        assertEquals("alice_new@mail.com", fromDb.getEmail());
    }

    @Test
    @DisplayName("Обновление несуществующего пользователя должно выбросить исключение")
    void updateNonexistentUser() {
        User fake = User.builder()
                .id(999L)
                .email("fake@mail.com")
                .login("fake")
                .name("Fake User")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();

        assertThrows(NotFoundException.class, () -> userDao.updateUser(fake));
    }
}