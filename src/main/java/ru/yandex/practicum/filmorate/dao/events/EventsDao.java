package ru.yandex.practicum.filmorate.dao.events;

import ru.yandex.practicum.filmorate.entity.Event;

import java.util.List;

public interface EventsDao {

    Event addEvent(Event event);

    List<Event> getUserEvents(Long id);
}
