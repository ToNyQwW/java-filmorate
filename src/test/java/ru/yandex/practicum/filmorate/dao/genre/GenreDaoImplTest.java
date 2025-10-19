package ru.yandex.practicum.filmorate.dao.genre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GenreDaoImplTest {

    @Autowired
    private GenreDao genreDao;

    @Test
    @DisplayName("Получение всех жанров")
    void getAllGenres() {
        List<Genre> genres = genreDao.getAllGenres();

        assertNotNull(genres);
        assertEquals(6, genres.size());
    }

    @Test
    @DisplayName("Получение жанра по ID")
    void getGenreById() {
        Long comedyId = genreDao.getAllGenres().stream()
                .filter(g -> "Комедия".equals(g.getName()))
                .findFirst()
                .get()
                .getId();

        Optional<Genre> genre = genreDao.getGenreById(comedyId);

        assertEquals("Комедия", genre.get().getName());
    }

    @Test
    @DisplayName("Получение жанра по несуществующему ID")
    void getGenreByNonExistentId() {
        Optional<Genre> genreOptional = genreDao.getGenreById(999L);
        assertTrue(genreOptional.isEmpty());
    }

    @Test
    @DisplayName("Получение жанров по списку ID")
    void getGenresByListId() {
        List<Long> ids = genreDao.getAllGenres().stream()
                .filter(g -> g.getName().equals("Комедия") || g.getName().equals("Драма"))
                .map(Genre::getId)
                .toList();

        List<Genre> genres = genreDao.getGenresByListId(ids);

        assertNotNull(genres);
        assertEquals(2, genres.size());
        assertTrue(genres.stream().anyMatch(g -> "Комедия".equals(g.getName())));
        assertTrue(genres.stream().anyMatch(g -> "Драма".equals(g.getName())));
    }
}