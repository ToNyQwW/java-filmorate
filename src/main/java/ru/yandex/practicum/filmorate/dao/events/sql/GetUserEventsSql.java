package ru.yandex.practicum.filmorate.dao.events.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GetUserEventsSql {

    private static final String GET_USER_EVENTS_SQL = """
            SELECT event_id,
                   entity_id,
                   timestamp,
                   event_type,
                   operation,
                   user_id
            FROM events
            WHERE user_id = ?
            """;

    public static String create() {
        return GET_USER_EVENTS_SQL;
    }
}
