package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.dao.filmGenre.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.dao.likes.LikesDao;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final MpaDao mpaDao;
    private final FilmDao filmDao;
    private final LikesDao likesDao;
    private final GenreDao genreDao;
    private final FilmGenreDao filmGenreDao;

    public Film addFilm(Film film) {
        throwIfMpaIdNotExists(film.getMpa().getId());
        var genres = film.getGenres();
        if (genres != null) {
            throwIfGenresNotExists(genres);
        }

        var addedFilm = filmDao.addFilm(film);
        if (genres != null && !genres.isEmpty()) {
            filmGenreDao.addFilmGenres(addedFilm.getId(), genres);
        }

        var result = filmDao.getFilm(addedFilm.getId()).get();
        log.info("Film added: {}", result);
        return result;
    }

    @Override
    public Film getFilm(Long id) {
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
    public Film updateFilm(Film film) {
        var updatedFilm = filmDao.updateFilm(film);
        log.info("Film updated: {}", updatedFilm);
        return updatedFilm;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        likesDao.addLike(filmId, userId);
        log.info("Added like for filmId: {}, userId: {}", filmId, userId);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        likesDao.removeLike(filmId, userId);
        log.info("Removed like for filmId: {}, userId: {}", filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        var popularFilms = filmDao.getPopularFilms(count);
        log.info("Number of popular films found: {}", popularFilms.size());
        return popularFilms;
    }

    private void throwIfMpaIdNotExists(Long mpaId) {
        var mpa = mpaDao.getMpaById(mpaId);

        if (mpa.isEmpty()) {
            throw new NotFoundException("Mpa with id " + mpaId + " not found");
        }
    }

    private void throwIfGenresNotExists(Set<Genre> genres) {
        var genresIds = genres.stream()
                .map(Genre::getId)
                .toList();
        if (genresIds.size() != genreDao.getGenresByListId(genresIds).size()) {
            throw new NotFoundException("Genre(s) not found");
        }
    }
}