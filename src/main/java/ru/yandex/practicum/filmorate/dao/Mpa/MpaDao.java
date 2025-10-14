package ru.yandex.practicum.filmorate.dao.Mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDao {

    private static final String GET_MPA_SQL = """
                    SELECT mpa_id as id,
                           name
                    FROM mpa
            """;

    private static final String GET_MPA_BY_ID_SQL = """
                    SELECT mpa_id as id,
                           name
                    FROM mpa
                    WHERE mpa_id = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query(GET_MPA_SQL, new BeanPropertyRowMapper<>(Mpa.class));
    }

    public Mpa getMpaById(Long id) {
        return jdbcTemplate.queryForObject(GET_MPA_BY_ID_SQL, new BeanPropertyRowMapper<>(Mpa.class), id);
    }
}