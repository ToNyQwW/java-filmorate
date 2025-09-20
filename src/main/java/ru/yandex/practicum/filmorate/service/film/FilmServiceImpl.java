package ru.yandex.practicum.filmorate.service.film;

import jakarta.validation.constraints.Min;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Film addFilm(Film film) {
        var addedFilm = filmStorage.addFilm(film);
        log.info("Film added: {}", film);
        return addedFilm;
    }

    @Override
    public Film getFilm(int id) throws NotFoundException {
        var film = filmStorage.getFilm(id);

        if (film.isPresent()) {
            log.info("Film found: {}", film);
            return film.get();
        } else {
            log.info("Film not found: {}", id);
            throw new NotFoundException("Film with id " + id + " not found");
        }
    }

    @Override
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @Override
    public Film updateFilm(Film film) throws NotFoundException {
        try {
            var updatedFilm = filmStorage.updateFilm(film);
            log.info("Film updated: {}", updatedFilm);
            return updatedFilm;
        } catch (NotFoundException e) {
            log.info("Film not found: {}", film);
            throw new NotFoundException("Film with id " + film.getId() + " not found");
        }
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
