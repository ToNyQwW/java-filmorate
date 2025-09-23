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
    public boolean containsId(Long id) {
        return this.films.containsKey(id);
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

    private void checkFIlmId(Long filmId) throws NotFoundException {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Film with id " + filmId + " not found");
        }
    }

    private void checkUserId(Long userId) throws NotFoundException {
        if (!userStorage.containsUserId(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }

    @Override
    public Film updateFilm(Film film) {
        var filmId = film.getId();
        checkFIlmId(filmId);

        films.put(filmId, film);
        return film;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        checkFIlmId(filmId);
        checkUserId(userId);

        films.get(filmId).addLike(userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        checkFIlmId(filmId);
        checkUserId(userId);

        films.get(filmId).removeLike(userId);
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        return films.values().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount).reversed())
                .limit(count)
                .toList();
    }
}