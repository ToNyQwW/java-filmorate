package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.dao.likes.LikesDao;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.dao.user.UserDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmDao filmDao;

    private final UserDao userDao;

    private final LikesDao likesDao;

    private final MpaDao mpaDao;

    private final GenreDao genreDao;

    @Override
    public Film addFilm(Film film) {
        throwIfMpaIdNotExists(film.getMpa().getId());
        for (var genre : film.getGenres()) {
            throwIfGenreIdNotExists(genre.getId());
        }
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
            throwIfFilmIdNotExists(film.getId());
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
            throwIfFilmIdNotExists(filmId);
            throwIfUserIdNotExists(userId);
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
            throwIfFilmIdNotExists(filmId);
            throwIfUserIdNotExists(userId);
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

    private void throwIfFilmIdNotExists(Long filmId) throws NotFoundException {
        var film = filmDao.getFilm(filmId);

        if (film.isEmpty()) {
            throw new NotFoundException("Film with id " + filmId + " not found");
        }
    }

    private void throwIfUserIdNotExists(Long userId) throws NotFoundException {
        var user = userDao.getUser(userId);

        if (user.isEmpty()) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }

    private void throwIfMpaIdNotExists(Long mpaId) throws NotFoundException {
        var mpa = mpaDao.getMpaById(mpaId);

        if (mpa.isEmpty()) {
            throw new NotFoundException("Mpa with id " + mpaId + " not found");
        }
    }

    private void throwIfGenreIdNotExists(Long genreId) throws NotFoundException {
        var genre = genreDao.getGenreById(genreId);

        if (genre.isEmpty()) {
            throw new NotFoundException("Genre with id " + genreId + " not found");
        }
    }
}