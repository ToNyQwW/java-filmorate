package ru.yandex.practicum.filmorate.dao.film;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.entity.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
@AllArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {

    private final MpaDao mpaDao;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        var mpa = mpaDao.getMpaById(rs.getLong("mpa_id"));

        return Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getObject("release_date", LocalDate.class))
                .duration(rs.getInt("duration"))
                .mpa(mpa)
                .build();
    }
}
