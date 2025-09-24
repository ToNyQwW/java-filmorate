package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final UserStorage userStorage;

    private final Map<Long, Film> films;

    private Long id;

    @Autowired
    public InMemoryFilmStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
        this.films = new HashMap<>();
        id = 0L;
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Optional<Film> getFilm(Long id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    private void containsFilmId(Long filmId) throws NotFoundException {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Film with id " + filmId + " not found");
        }
    }

    @Override
    public Film updateFilm(Film film) throws NotFoundException {
        var filmId = film.getId();
        containsFilmId(filmId);

        films.put(filmId, film);
        return film;
    }

    private void containsUserId(Long userId) throws NotFoundException {
        if (!userStorage.containsUserId(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }

    private void checkFilm(Long id, Film film) throws NotFoundException {
        if (film == null) {
            throw new NotFoundException("Film with id " + id + " not found");
        }
    }

    @Override
    public void addLike(Long filmId, Long userId) throws NotFoundException {
        var film = films.get(filmId);

        checkFilm(filmId, film);

        containsUserId(userId);
        film.addLike(userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) throws NotFoundException {
        var film = films.get(filmId);

        checkFilm(filmId, film);

        containsUserId(userId);
        film.removeLike(userId);
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        return films.values().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(count)
                .toList();
    }
}