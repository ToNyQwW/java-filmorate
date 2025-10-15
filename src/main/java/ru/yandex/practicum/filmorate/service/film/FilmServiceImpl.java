package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;

import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {

    private final FilmDao filmStorage;

    @Autowired
    public FilmServiceImpl(FilmDao filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public Film addFilm(Film film) {
        var addedFilm = filmStorage.addFilm(film);
        log.info("Film added: {}", film);
        return addedFilm;
    }

    @Override
    public Film getFilm(Long id) throws NotFoundException {
        var film = filmStorage.getFilm(id);
        if (film.isPresent()) {
            var findedFilm = film.get();
            log.info("Film found: {}", findedFilm);
            return findedFilm;
        }
        log.error("Film with id {} not found", id);
        throw new NotFoundException("Film with id " + id + " not found");
    }

    @Override
    public List<Film> getFilms() {
        var films = filmStorage.getFilms();
        log.info("Number of films found: {}", films.size());
        return films;
    }

    @Override
    public Film updateFilm(Film film) throws NotFoundException {
        try {
            var updatedFilm = filmStorage.updateFilm(film);
            log.info("Film updated: {}", updatedFilm);
            return updatedFilm;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        try {
            filmStorage.addLike(filmId, userId);
            log.info("Added like for filmId: {}, userId: {}", filmId, userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        try {
            filmStorage.removeLike(filmId, userId);
            log.info("Removed like for filmId: {}, userId: {}", filmId, userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        var popularFilms = filmStorage.getPopularFilms(count);
        log.info("Number of popular films found: {}", popularFilms.size());
        return popularFilms;
    }
}