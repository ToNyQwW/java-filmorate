package ru.yandex.practicum.filmorate.service.events;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.events.EventsDao;
import ru.yandex.practicum.filmorate.entity.Event;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EventsServiceImpl implements EventsService {

    private final EventsDao eventsDao;

    @Override
    public Event createEvent(Event event) {
        var createdEvent = eventsDao.addEvent(event);
        log.info("Event created: {}", createdEvent);
        return createdEvent;
    }

    @Override
    public List<Event> getUserEvents(Long id) {
        var userEvents = eventsDao.getUserEvents(id);
        log.info("Number of events found: {}", userEvents.size());
        return userEvents;
    }
}