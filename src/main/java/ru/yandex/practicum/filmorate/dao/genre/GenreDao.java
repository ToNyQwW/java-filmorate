package ru.yandex.practicum.filmorate.dao.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDao {

    private static final String GET_GENRE_SQL = """
                    SELECT genre_id as id,
                           name
                    FROM genre
            """;

    private static final String GET_GENRE_BY_ID_SQL = """
                    SELECT genre_id as id,
                           name
                    FROM genre
                    WHERE genre_id = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    public List<Genre> getAllGenres() {
        return jdbcTemplate.query(GET_GENRE_SQL, new BeanPropertyRowMapper<>(Genre.class));
    }

    public Genre getGenreById(Long id) {
        return jdbcTemplate.queryForObject(GET_GENRE_BY_ID_SQL, new BeanPropertyRowMapper<>(Genre.class), id);
    }
}
