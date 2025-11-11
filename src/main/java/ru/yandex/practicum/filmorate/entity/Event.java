package ru.yandex.practicum.filmorate.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;

@Data
@Builder
@EqualsAndHashCode(of = "id")
public class Event {

    private Long id;

    private Long entityId;

    private Long timestamp;

    private EventType eventType;

    private EventOperation operation;

    private Long userId;
}