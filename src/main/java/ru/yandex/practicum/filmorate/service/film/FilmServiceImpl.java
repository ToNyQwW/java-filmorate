package ru.yandex.practicum.filmorate.service.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
        var films = filmStorage.getFilms();
        log.info("Number of films found: {}", films.size());
        return films;
    }

    @Override
    public Film updateFilm(Film film) throws NotFoundException {
        validateFilmId(film.getId());

        var updatedFilm = filmStorage.updateFilm(film);
        log.info("Film updated: {}", updatedFilm);
        return updatedFilm;
    }

    @Override
    public void addLike(int filmId, int userId) {
        validateFilmId(filmId);
        validateUserId(userId);

        filmStorage.addLike(filmId, userId);
        log.info("Added like for filmId: {}, userId: {}", filmId, userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        validateFilmId(filmId);
        validateUserId(userId);

        filmStorage.removeLike(filmId, userId);
        log.info("Removed like for filmId: {}, userId: {}", filmId, userId);
    }

    private void validateFilmId(int filmId) throws NotFoundException {
        if (!filmStorage.containsId(filmId)) {
            log.info("FilmId not found: {}", filmId);
            throw new NotFoundException("Film with id " + filmId + " not found");
        }
    }

    private void validateUserId(int userId) throws NotFoundException {
        if (!userStorage.containsUserId(userId)) {
            log.info("UserId not found: {}", userId);
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        var popularFilms = filmStorage.getPopularFilms(count);
        log.info("Number of popular films found: {}", popularFilms.size());
        return popularFilms;
    }
}
