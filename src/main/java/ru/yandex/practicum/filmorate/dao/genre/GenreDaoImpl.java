package ru.yandex.practicum.filmorate.dao.genre;

import lombok.AllArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private static final String GET_GENRES_SQL = """
                    SELECT genre_id,
                           name
                    FROM genre
            """;

    private static final String GET_GENRE_BY_ID_SQL = """
                    SELECT genre_id,
                           name
                    FROM genre
                    WHERE genre_id = ?
            """;

    private static final String GET_GENRES_BY_IDS_SQL = """
                    SELECT genre_id,
                           name
                    FROM genre
                    WHERE genre_id IN (:ids)
            """;

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query(GET_GENRES_SQL, genreRowMapper);
    }

    @Override
    public Optional<Genre> getGenreById(Long id) {
        var genres = jdbcTemplate.query(GET_GENRE_BY_ID_SQL, genreRowMapper, id);
        var genre = DataAccessUtils.singleResult(genres);
        return Optional.ofNullable(genre);
    }

    @Override
    public List<Genre> getGenresByListId(List<Long> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);

        return namedParameterJdbcTemplate.query(GET_GENRES_BY_IDS_SQL, params, genreRowMapper);
    }
}