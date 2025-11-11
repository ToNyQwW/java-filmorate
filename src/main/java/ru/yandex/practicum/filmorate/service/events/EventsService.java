package ru.yandex.practicum.filmorate.service.events;

import ru.yandex.practicum.filmorate.entity.Event;

import java.util.List;

public interface EventsService {

    Event createEvent(Event event);

    List<Event> getUserEvents(Long id);
}