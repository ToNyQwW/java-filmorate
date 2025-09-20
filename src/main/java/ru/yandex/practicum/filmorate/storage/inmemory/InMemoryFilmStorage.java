package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films;

    private Long id;

    public InMemoryFilmStorage() {
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
    public Film getFilm(Long id) {
        return films.get(id);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        films.get(filmId).addLike(userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        films.get(filmId).removeLike(userId);
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        return films.values().stream()
                .sorted(Comparator.comparingInt(Film::getLikesCount))
                .limit(count)
                .toList();
    }
}
