package ru.yandex.practicum.filmorate.dao.filmGenre;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.filmGenre.sql.AddFilmGenresSql;
import ru.yandex.practicum.filmorate.dao.filmGenre.sql.GetFilmGenresSql;
import ru.yandex.practicum.filmorate.dao.filmGenre.sql.GetFilmsGenresByListFilmsIdsSql;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.*;

@Repository
@AllArgsConstructor
public class FilmGenreDaoImpl implements FilmGenreDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void addFilmGenres(Long filmId, Set<Genre> genres) {
        jdbcTemplate.batchUpdate(AddFilmGenresSql.create(), genres, genres.size(),
                (ps, genre) -> {
                    ps.setLong(1, filmId);
                    ps.setLong(2, genre.getId());
                }
        );
    }

    @Override
    public List<Long> getFilmGenres(Long filmId) {
        return jdbcTemplate.queryForList(GetFilmGenresSql.create(), Long.class, filmId);
    }

    @Override
    public Map<Long, List<Long>> getFilmsGenresByListFilmIds(List<Long> filmIds) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmsIds", filmIds);

        return namedParameterJdbcTemplate.query(GetFilmsGenresByListFilmsIdsSql.create(), params, rs -> {
            Map<Long, List<Long>> result = new HashMap<>();

            while (rs.next()) {
                var filmId = rs.getLong("film_id");
                var genreId = rs.getLong("genre_id");

                result.computeIfAbsent(filmId, value -> new ArrayList<>())
                        .add(genreId);
            }

            return result;
        });
    }
}