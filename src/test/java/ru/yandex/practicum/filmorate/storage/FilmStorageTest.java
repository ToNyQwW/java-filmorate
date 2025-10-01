package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmStorageTest {

    @Autowired
    private FilmStorage filmStorage;

    @Autowired
    private UserStorage userStorage;

    private Film createFilm(String name) {
        Film film = new Film();
        film.setName(name);
        film.setDescription("Description of " + name);
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        return film;
    }

    private User createUser() {
        User user = new User();
        user.setName("user");
        user.setEmail("email@email");
        user.setLogin("login");
        user.setBirthday(LocalDate.now());
        return user;
    }

    @Test
    void addFilm() {
        Film film = createFilm("Matrix");

        Film saved = filmStorage.addFilm(film);

        assertNotNull(saved.getId());
        assertNotNull(filmStorage.getFilm(saved.getId()));
        assertEquals("Matrix", filmStorage.getFilm(saved.getId()).get().getName());
    }

    @Test
    void updateFilm() {
        Film film = createFilm("Matrix");
        Film saved = filmStorage.addFilm(film);

        saved.setName("Matrix Reloaded");
        Film updated = filmStorage.updateFilm(saved);

        assertEquals("Matrix Reloaded", filmStorage.getFilm(saved.getId()).get().getName());
        assertEquals(updated, filmStorage.getFilm(saved.getId()).get());
    }

    @Test
    void getPopularFilms() {
        Film f1 = filmStorage.addFilm(createFilm("Film1"));
        Film f2 = filmStorage.addFilm(createFilm("Film2"));
        Film f3 = filmStorage.addFilm(createFilm("Film3"));

        User user1 = userStorage.createUser(createUser());
        User user2 = userStorage.createUser(createUser());
        User user3 = userStorage.createUser(createUser());

        filmStorage.addLike(f2.getId(), user1.getId());
        filmStorage.addLike(f1.getId(), user1.getId());
        filmStorage.addLike(f1.getId(), user2.getId());
        filmStorage.addLike(f1.getId(), user3.getId());
        filmStorage.addLike(f3.getId(), user1.getId());
        filmStorage.addLike(f3.getId(), user2.getId());

        List<Film> popular = filmStorage.getPopularFilms(10L);

        assertEquals(List.of(f1, f3, f2), popular);
    }
}