package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validation.validFilm.ValidFilm;
import ru.yandex.practicum.filmorate.validation.validFilmId.ValidFilmId;
import ru.yandex.practicum.filmorate.validation.validUserId.ValidUserId;

import java.util.List;

@Slf4j
@Validated
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
    public Film getFilm(@ValidFilmId Long id) {
        var film = filmStorage.getFilm(id);
        log.info("Film get: {}", film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        var films = filmStorage.getFilms();
        log.info("Number of films found: {}", films.size());
        return films;
    }

    @Override
    public Film updateFilm(@ValidFilm Film film) {
        var updatedFilm = filmStorage.updateFilm(film);
        log.info("Film updated: {}", updatedFilm);
        return updatedFilm;
    }

    @Override
    public void addLike(@ValidFilmId Long filmId, @ValidUserId Long userId) {
        filmStorage.addLike(filmId, userId);
        log.info("Added like for filmId: {}, userId: {}", filmId, userId);
    }

    @Override
    public void removeLike(@ValidFilmId Long filmId, @ValidUserId Long userId) {
        filmStorage.removeLike(filmId, userId);
        log.info("Removed like for filmId: {}, userId: {}", filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        var popularFilms = filmStorage.getPopularFilms(count);
        log.info("Number of popular films found: {}", popularFilms.size());
        return popularFilms;
    }
}
