package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.AllArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class MpaDaoImpl implements MpaDao {

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
    private final MpaRowMapper mpaRowMapper;

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query(GET_MPA_SQL, mpaRowMapper);
    }

    @Override
    public Optional<Mpa> getMpaById(Long id) {
        var result = jdbcTemplate.query(GET_MPA_BY_ID_SQL, mpaRowMapper, id);
        var mpa = DataAccessUtils.singleResult(result);
        return Optional.ofNullable(mpa);
    }
}