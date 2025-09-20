package ru.yandex.practicum.filmorate.storage.inmemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films;

    private int id;

    public InMemoryFilmStorage() {
        this.films = new HashMap<>();
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Film added: {}", film);
        return film;
    }

    @Override
    public Optional<Film> getFilm(int id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film updateFilm(Film film) throws NotFoundException {
        int filmId = film.getId();
        if (!films.containsKey(filmId)) {
            log.warn("Film not found: {}", film);
            throw new NotFoundException("Film not found");
        }
        films.put(filmId, film);
        log.info("Film updated: {}", film);
        return film;
    }

    @Override
    public void addLike(int filmId, int userId) {
        films.get(filmId).addLike(userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        films.get(filmId).removeLike(userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return films.values().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount))
                .limit(count)
                .toList();
    }
}
