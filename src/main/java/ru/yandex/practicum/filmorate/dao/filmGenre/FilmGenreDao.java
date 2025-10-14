package ru.yandex.practicum.filmorate.dao.filmGenre;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.entity.FilmGenres;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenreDao {

    private static final String GET_FILM_GENRES_SQL = """
                    SELECT genre_id
                    FROM film_genre
                    WHERE film_id = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    private final GenreDao genreDao;

    public FilmGenres getFilmGenresId(Long filmId) {
        List<Long> genreIds = jdbcTemplate.query(GET_FILM_GENRES_SQL,
                (rs, rowNum) -> rs.getLong("genre_id"),
                filmId);

        return new FilmGenres(filmId, genreDao.getGenresByListId(genreIds));
    }
}