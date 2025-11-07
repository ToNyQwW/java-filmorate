package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.AllArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mpa.sql.GetAllMpaSql;
import ru.yandex.practicum.filmorate.dao.mpa.sql.GetMpaByIdSql;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query(GetAllMpaSql.create(), mpaRowMapper);
    }

    @Override
    public Optional<Mpa> getMpaById(Long id) {
        var result = jdbcTemplate.query(GetMpaByIdSql.create(), mpaRowMapper, id);
        var mpa = DataAccessUtils.singleResult(result);
        return Optional.ofNullable(mpa);
    }
}