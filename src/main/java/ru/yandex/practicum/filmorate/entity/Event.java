package ru.yandex.practicum.filmorate.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;

import java.sql.Timestamp;

@Data
@Builder
@EqualsAndHashCode(of = "id")
public class Event {

    private Long id;

    private Long entityId;

    private Timestamp timestamp;

    private EventType type;

    private EventOperation operation;

    private Long userId;
}