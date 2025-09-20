package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    @Override
    public Film getFilm(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("id must be greater than zero");
        }
        var film = filmStorage.getFilm(id);

        if (film.isPresent()) {
            return film.get();
        } else {
            throw new NotFoundException("Film with id " + id + " not found");
        }
    }

    @Override
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        filmStorage.removeLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}
