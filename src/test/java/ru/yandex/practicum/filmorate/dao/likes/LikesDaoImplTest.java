package ru.yandex.practicum.filmorate.dao.likes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LikesDaoImplTest {

    @Autowired
    private LikesDao likesDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long userId;
    private Long filmId;

    @BeforeEach
    void setUp() {
        Long mpaId = jdbcTemplate.queryForObject("SELECT mpa_id FROM mpa WHERE name='PG' LIMIT 1", Long.class);

        jdbcTemplate.update(
                "INSERT INTO film (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)",
                "Test Film", "Description", LocalDate.of(2020, 1, 1), 120, mpaId
        );
        filmId = jdbcTemplate.queryForObject("SELECT film_id FROM film LIMIT 1", Long.class);

        jdbcTemplate.update(
                "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)",
                "user@mail.com", "userlogin", "User", LocalDate.of(1990, 1, 1)
        );
        userId = jdbcTemplate.queryForObject("SELECT user_id FROM users LIMIT 1", Long.class);
    }

    @Test
    @DisplayName("Добавление и получение лайка")
    void addAndGetLike() {
        likesDao.addLike(filmId, userId);

        List<Long> likes = likesDao.getFilmLikes(filmId);
        assertNotNull(likes);
        assertEquals(1, likes.size());
        assertTrue(likes.contains(userId));
    }

    @Test
    @DisplayName("Удаление лайка")
    void removeLike() {
        likesDao.addLike(filmId, userId);

        likesDao.removeLike(filmId, userId);

        List<Long> likes = likesDao.getFilmLikes(filmId);
        assertNotNull(likes);
        assertTrue(likes.isEmpty());
    }

    @Test
    @DisplayName("Удаление несуществующего лайка выбрасывает NotFoundException")
    void removeNonExistentLike() {
        assertThrows(NotFoundException.class, () -> likesDao.removeLike(filmId, userId));
    }

    @Test
    @DisplayName("Получение популярных фильмов")
    void getPopularFilmIds() {
        likesDao.addLike(filmId, userId);

        List<Long> popularFilms = likesDao.getPopularFilmIds(10L);
        assertNotNull(popularFilms);
        assertTrue(popularFilms.contains(filmId));
    }

    @Test
    @DisplayName("Получение лайков для списка фильмов")
    void getUserLikesByFilmIds() {
        likesDao.addLike(filmId, userId);

        Map<Long, List<Long>> likesMap = likesDao.getUserLikesByFilmIds(List.of(filmId));
        assertNotNull(likesMap);
        assertTrue(likesMap.containsKey(filmId));
        assertTrue(likesMap.get(filmId).contains(userId));
    }
}