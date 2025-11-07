package ru.yandex.practicum.filmorate.dao.film;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDaoImplTest {

    @Autowired
    private FilmDaoImpl filmDao;

    @Test
    @DisplayName("Добавление фильма — при корректных данных фильм сохраняется и читается из БД корректно")
    void addAndGetFilm() {
        Film film = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(2010, 7, 16))
                .duration(148)
                .mpa(Mpa.builder().id(3L).name("PG-13").build())
                .build();

        filmDao.addFilm(film);
        assertNotNull(film.getId(), "ID должен быть сгенерирован");

        Optional<Film> fromDb = filmDao.getFilm(film.getId());
        assertTrue(fromDb.isPresent(), "Фильм должен быть найден");

        Film loaded = fromDb.get();
        assertEquals("film", loaded.getName());
        assertEquals("description", loaded.getDescription());
        assertEquals(148, loaded.getDuration());
        assertEquals(LocalDate.of(2010, 7, 16), loaded.getReleaseDate());
        assertEquals(3L, loaded.getMpa().getId());
        assertEquals("PG-13", loaded.getMpa().getName());
    }

    @Test
    @DisplayName("Обновление существующего фильма — поля корректно изменяются в БД")
    void updateFilm() {
        Film film = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(1999, 3, 31))
                .duration(136)
                .mpa(Mpa.builder().id(4L).name("R").build())
                .build();

        filmDao.addFilm(film);

        film.setName("film updated");
        film.setDescription("description updated");
        film.setDuration(138);
        film.setMpa(Mpa.builder().id(2L).name("PG").build());

        filmDao.updateFilm(film);

        Optional<Film> updatedOpt = filmDao.getFilm(film.getId());
        assertTrue(updatedOpt.isPresent());
        Film updated = updatedOpt.get();

        assertEquals("film updated", updated.getName());
        assertEquals("description updated", updated.getDescription());
        assertEquals(138, updated.getDuration());
        assertEquals(2L, updated.getMpa().getId());
        assertEquals("PG", updated.getMpa().getName());
    }

    @Test
    @DisplayName("Получение списка фильмов — при наличии записей возвращается полный список List<Film>")
    void getAllFilms() {
        Film film1 = Film.builder()
                .name("film1")
                .description("description1")
                .releaseDate(LocalDate.of(2014, 11, 7))
                .duration(169)
                .mpa(Mpa.builder().id(3L).name("PG-13").build())
                .build();

        Film film2 = Film.builder()
                .name("film2")
                .description("description2")
                .releaseDate(LocalDate.of(2020, 8, 26))
                .duration(150)
                .mpa(Mpa.builder().id(4L).name("R").build())
                .build();

        filmDao.addFilm(film1);
        filmDao.addFilm(film2);

        List<Film> allFilms = filmDao.getFilms();

        assertEquals(2, allFilms.size());
        List<String> names = allFilms.stream().map(Film::getName).toList();
        assertTrue(names.containsAll(List.of("film1", "film2")));
    }

    @Test
    @DisplayName("Обновление несуществующего фильма вызывает исключение")
    void updateNonExistingFilmThrows() {
        Film film = Film.builder()
                .id(999L)
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .mpa(Mpa.builder().id(1L).name("G").build())
                .build();

        assertThrows(RuntimeException.class, () -> filmDao.updateFilm(film));
    }
}