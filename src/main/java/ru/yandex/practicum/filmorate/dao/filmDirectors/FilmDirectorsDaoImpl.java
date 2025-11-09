package ru.yandex.practicum.filmorate.dao.filmDirectors;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.filmDirectors.sql.AddFilmDirectorSql;
import ru.yandex.practicum.filmorate.dao.filmDirectors.sql.GetFilmDirectorsSql;
import ru.yandex.practicum.filmorate.dao.filmDirectors.sql.GetFilmsDirectorsByListFilmsIdsSql;
import ru.yandex.practicum.filmorate.entity.Director;

import java.util.*;

@Repository
@AllArgsConstructor
public class FilmDirectorsDaoImpl implements FilmDirectorsDao {


    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void addFilmDirector(Long filmId, Set<Director> directors) {
        jdbcTemplate.batchUpdate(AddFilmDirectorSql.create(), directors, directors.size(),
                (ps, director) -> {
                    ps.setLong(1, filmId);
                    ps.setLong(2, director.getId());
                }
        );
    }

    @Override
    public List<Long> getFilmDirectors(Long filmId) {
        return jdbcTemplate.queryForList(GetFilmDirectorsSql.create(), Long.class, filmId);
    }

    @Override
    public Map<Long, List<Long>> getFilmsDirectorsByListFilmIds(List<Long> filmIds) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("filmsIds", filmIds);

        return namedParameterJdbcTemplate.query(GetFilmsDirectorsByListFilmsIdsSql.create(), params, rs -> {
            Map<Long, List<Long>> result = new HashMap<>();

            while (rs.next()) {
                var filmId = rs.getLong("film_id");
                var directorId = rs.getLong("director_id");

                result.computeIfAbsent(filmId, value -> new ArrayList<>())
                        .add(directorId);
            }

            return result;
        });
    }
}