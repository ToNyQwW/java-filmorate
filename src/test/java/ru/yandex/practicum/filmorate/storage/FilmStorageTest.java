package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.inmemory.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmStorageTest {

    private FilmStorage filmStorage;

    @BeforeEach
    void setUp() {
        filmStorage = new InMemoryFilmStorage();
    }

    private Film createFilm(String name) {
        Film film = new Film();
        film.setName(name);
        film.setDescription("Description of " + name);
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        return film;
    }

    @Test
    void addFilm() {
        Film film = createFilm("Matrix");

        Film saved = filmStorage.addFilm(film);

        assertNotNull(saved.getId());
        assertTrue(filmStorage.containsId(saved.getId()));
        assertEquals("Matrix", filmStorage.getFilm(saved.getId()).getName());
    }

    @Test
    void updateFilm() {
        Film film = createFilm("Matrix");
        Film saved = filmStorage.addFilm(film);

        saved.setName("Matrix Reloaded");
        Film updated = filmStorage.updateFilm(saved);

        assertEquals("Matrix Reloaded", filmStorage.getFilm(saved.getId()).getName());
        assertEquals(updated, filmStorage.getFilm(saved.getId()));
    }

    @Test
    void getPopularFilms() {
        Film f1 = filmStorage.addFilm(createFilm("Film1"));
        Film f2 = filmStorage.addFilm(createFilm("Film2"));
        Film f3 = filmStorage.addFilm(createFilm("Film3"));

        filmStorage.addLike(f2.getId(), 1L);
        filmStorage.addLike(f1.getId(), 1L);
        filmStorage.addLike(f1.getId(), 2L);
        filmStorage.addLike(f1.getId(), 3L);
        filmStorage.addLike(f3.getId(), 1L);
        filmStorage.addLike(f3.getId(), 2L);

        List<Film> popular = filmStorage.getPopularFilms(10L);

        assertEquals(List.of(f1, f3, f2), popular);
    }
}