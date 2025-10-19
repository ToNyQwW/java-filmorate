package ru.yandex.practicum.filmorate.dao.filmGenre;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmGenreDaoImplTest {

    @Autowired
    private FilmGenreDao filmGenreDao;

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Long filmId;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("""
                INSERT INTO film (name, description, release_date, duration, mpa_id)
                VALUES (?, ?, ?, ?, ?)
                """, "Test Film", "Description", LocalDate.of(2020, 1, 1), 120, 1);

        filmId = jdbcTemplate.queryForObject("SELECT film_id FROM film WHERE name = ?", Long.class, "Test Film");
    }

    @Test
    @DisplayName("Добавление и получение жанров фильма")
    void addAndGetFilmGenres() {
        Genre comedy = findGenreByName("Комедия");
        Genre drama = findGenreByName("Драма");

        filmGenreDao.addFilmGenres(filmId, Set.of(comedy, drama));

        List<Genre> filmGenres = filmGenreDao.getFilmGenres(filmId);

        assertNotNull(filmGenres);
        assertEquals(2, filmGenres.size());

        List<String> genreNames = filmGenres.stream()
                .map(Genre::getName)
                .toList();

        assertTrue(genreNames.contains("Комедия"));
        assertTrue(genreNames.contains("Драма"));
    }

    @Test
    @DisplayName("Получение жанров для списка фильмов")
    void getFilmsGenresByListFilmIds() {
        Genre comedy = findGenreByName("Комедия");
        Genre drama = findGenreByName("Драма");

        filmGenreDao.addFilmGenres(filmId, Set.of(comedy, drama));

        Map<Long, List<Genre>> result = filmGenreDao.getFilmsGenresByListFilmIds(List.of(filmId));

        assertNotNull(result);
        assertTrue(result.containsKey(filmId));

        List<Genre> genres = result.get(filmId);
        List<String> genreNames = genres.stream()
                .map(Genre::getName)
                .toList();

        assertTrue(genreNames.contains("Комедия"));
        assertTrue(genreNames.contains("Драма"));
    }

    private Genre findGenreByName(String name) {
        return genreDao.getAllGenres().stream()
                .filter(g -> g.getName().equals(name))
                .findFirst()
                .get();
    }
}