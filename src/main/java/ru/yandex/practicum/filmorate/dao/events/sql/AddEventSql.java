package ru.yandex.practicum.filmorate.dao.events.sql;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AddEventSql {

    private static final String ADD_EVENT_SQL = """
            INSERT INTO events
            (entity_id, timestamp, event_type, operation, user_id)
            VALUES (:entityId, :timestamp, :eventType, :operation, :userId)
            """;

    public static String create() {
        return ADD_EVENT_SQL;
    }
}
