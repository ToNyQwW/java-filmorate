package ru.yandex.practicum.filmorate.dao.events;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.events.sql.AddEventSql;
import ru.yandex.practicum.filmorate.dao.events.sql.GetUserEventsSql;
import ru.yandex.practicum.filmorate.entity.Event;

import java.util.List;

@Repository
@AllArgsConstructor
public class EventsDaoImpl implements EventsDao {

    private final JdbcTemplate jdbcTemplate;
    private final EventRowMapper eventRowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Event addEvent(Event event) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("entityId", event.getEntityId())
                .addValue("timestamp", event.getTimestamp())
                .addValue("eventType", event.getType())
                .addValue("operation", event.getOperation())
                .addValue("userId", event.getUserId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(AddEventSql.create(), params, keyHolder);

        event.setId(keyHolder.getKey().longValue());

        return event;
    }

    @Override
    public List<Event> getUserEvents(Long id) {
        return jdbcTemplate.query(GetUserEventsSql.create(), eventRowMapper, id);
    }
}