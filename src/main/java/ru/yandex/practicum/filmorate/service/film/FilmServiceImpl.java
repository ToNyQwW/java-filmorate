package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.dao.likes.LikesDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmDao filmDao;

    private final LikesDao likesDao;

    @Override
    public Film addFilm(Film film) {
        var addedFilm = filmDao.addFilm(film);
        log.info("Film added: {}", film);
        return addedFilm;
    }

    @Override
    public Film getFilm(Long id) throws NotFoundException {
        var film = filmDao.getFilm(id);
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
        var films = filmDao.getFilms();
        log.info("Number of films found: {}", films.size());
        return films;
    }

    @Override
    public Film updateFilm(Film film) throws NotFoundException {
        try {
            var updatedFilm = filmDao.updateFilm(film);
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
            likesDao.addLike(filmId, userId);
            log.info("Added like for filmId: {}, userId: {}", filmId, userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        try {
            likesDao.removeLike(filmId, userId);
            log.info("Removed like for filmId: {}, userId: {}", filmId, userId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        var popularFilms = filmDao.getPopularFilms(count);
        log.info("Number of popular films found: {}", popularFilms.size());
        return popularFilms;
    }
}