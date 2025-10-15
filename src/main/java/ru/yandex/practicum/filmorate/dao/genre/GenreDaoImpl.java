package ru.yandex.practicum.filmorate.dao.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoImpl implements GenreDao {

    private static final String GET_GENRES_SQL = """
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

    private static final String GET_GENRES_BY_IDS_SQL = """
                    SELECT genre_id as id,
                           name
                    FROM genre
                    WHERE genre_id IN (:ids)
            """;

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query(GET_GENRES_SQL, new BeanPropertyRowMapper<>(Genre.class));
    }

    @Override
    public Genre getGenreById(Long id) {
        return jdbcTemplate.queryForObject(GET_GENRE_BY_ID_SQL, new BeanPropertyRowMapper<>(Genre.class), id);
    }

    @Override
    public List<Genre> getGenresByListId(List<Long> ids) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);

        return namedParameterJdbcTemplate.query(GET_GENRES_BY_IDS_SQL, params,
                new BeanPropertyRowMapper<>(Genre.class));
    }
}
