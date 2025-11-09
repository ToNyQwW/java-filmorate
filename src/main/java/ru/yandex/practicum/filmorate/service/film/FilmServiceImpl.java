package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.directors.DirectorsDao;
import ru.yandex.practicum.filmorate.dao.film.FilmDao;
import ru.yandex.practicum.filmorate.dao.filmDirectors.FilmDirectorsDao;
import ru.yandex.practicum.filmorate.dao.filmGenre.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.dao.likes.LikesDao;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.entity.Director;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.SortType;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final MpaDao mpaDao;
    private final FilmDao filmDao;
    private final LikesDao likesDao;
    private final GenreDao genreDao;
    private final DirectorsDao directorsDao;
    private final FilmGenreDao filmGenreDao;
    private final FilmDirectorsDao filmDirectorsDao;

    @Override
    public Film addFilm(Film film) {
        throwIfMpaIdNotExists(film.getMpa().getId());

        var genres = film.getGenres();
        if (genres != null) {
            throwIfGenresNotExists(genres);
        }

        var directors = film.getDirectors();
        if (directors != null) {
            throwIfDirectorsNotExists(directors);
        }

        var addedFilm = filmDao.addFilm(film);

        if (genres != null && !genres.isEmpty()) {
            filmGenreDao.addFilmGenres(addedFilm.getId(), genres);
        }

        if (directors != null && !directors.isEmpty()) {
            filmDirectorsDao.addFilmDirector(addedFilm.getId(), directors);
        }

        var result = getFilm(addedFilm.getId());
        log.info("Film added: {}", result);
        return result;
    }

    @Override
    public Film getFilm(Long id) {
        var film = filmDao.getFilm(id);
        if (film.isPresent()) {
            var findedFilm = film.get();
            var filmLikes = likesDao.getFilmLikes(id);

            var filmGenresIds = filmGenreDao.getFilmGenres(id);
            var filmGenres = genreDao.getGenresByListId(filmGenresIds);

            var filmDirectorsIds = filmDirectorsDao.getFilmDirectors(id);
            var filmDirectors = directorsDao.getDirectorsByListIds(filmDirectorsIds);

            findedFilm.setLikes(new LinkedHashSet<>(filmLikes));
            findedFilm.setGenres(new LinkedHashSet<>(filmGenres));
            findedFilm.setDirectors(new LinkedHashSet<>(filmDirectors));

            log.info("Film found: {}", findedFilm);
            return findedFilm;
        }
        log.error("Film with id {} not found", id);
        throw new NotFoundException("Film with id " + id + " not found");
    }

    @Override
    public List<Film> getFilms() {
        var films = filmDao.getFilms();
        buildFilms(films);
        log.info("Number of films found: {}", films.size());
        return films;
    }

    @Override
    public List<Film> getFilmsByDirectorId(Long id, SortType sortType) {
        var filmsIds = filmDirectorsDao.getFilmsByDirectorId(id);

        if (filmsIds.isEmpty()) {
            log.info("films by director id not found");
            return Collections.emptyList();
        }

        var films = filmDao.getFilmsByListIds(filmsIds);
        buildFilms(films);

        switch (sortType) {
            case YEAR -> sortFilmsByYear(films);
            case LIKES -> sortFilmsByLikes(films);
        }
        return films;
    }

    @Override
    public Film updateFilm(Film film) {
        throwIfMpaIdNotExists(film.getMpa().getId());

        var genres = film.getGenres();
        if (genres != null) {
            throwIfGenresNotExists(genres);
        }

        var directors = film.getDirectors();
        if (directors != null) {
            throwIfDirectorsNotExists(directors);
        }

        filmDao.updateFilm(film);

        if (genres != null && !genres.isEmpty()) {
            filmGenreDao.addFilmGenres(film.getId(), genres);
        }

        if (directors != null && !directors.isEmpty()) {
            filmDirectorsDao.addFilmDirector(film.getId(), directors);
        }

        var updatedFilm = getFilm(film.getId());
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
        var popularFilmIds = likesDao.getPopularFilmIds(count);

        if (popularFilmIds.isEmpty()) {
            log.info("No popular films found");
            return Collections.emptyList();
        }

        var popularFilms = filmDao.getFilmsByListIds(popularFilmIds);
        buildFilms(popularFilms);
        sortFilmsByPopularityOrder(popularFilms, popularFilmIds);
        log.info("Number of popular films found: {}", popularFilms.size());
        return popularFilms;
    }

    private void throwIfMpaIdNotExists(Long mpaId) {
        var mpa = mpaDao.getMpaById(mpaId);

        if (mpa.isEmpty()) {
            log.error("MPA id not found: {}", mpaId);
            throw new NotFoundException("Mpa with id " + mpaId + " not found");
        }
    }

    private void throwIfGenresNotExists(Set<Genre> genres) {
        var genresIds = genres.stream()
                .map(Genre::getId)
                .toList();
        if (genresIds.size() != genreDao.getGenresByListId(genresIds).size()) {
            log.error("Genres with id {} not found", genresIds);
            throw new NotFoundException("Genre(s) not found");
        }
    }

    private void throwIfDirectorsNotExists(Set<Director> directors) {
        var directorsIds = directors.stream()
                .map(Director::getId)
                .toList();
        if (directorsIds.size() != directorsDao.getDirectorsByListIds(directorsIds).size()) {
            log.error("Director(s) with id {} not found", directorsIds);
            throw new NotFoundException("Directors(s) not found");
        }
    }

    private void buildFilms(List<Film> films) {

        var filmsIds = films.stream().map(Film::getId).toList();

        var userLikes = likesDao.getUserLikesByFilmIds(filmsIds);

        var filmsGenresIds = filmGenreDao.getFilmsGenresByListFilmIds(filmsIds);
        var filmsGenres = getGenres(filmsGenresIds);

        var filmsDirectorsIds = filmDirectorsDao.getFilmsDirectorsByListFilmIds(filmsIds);
        var filmsDirectors = getDirectors(filmsDirectorsIds);

        for (Film film : films) {
            var id = film.getId();
            film.setLikes(new HashSet<>(userLikes.getOrDefault(id, Collections.emptyList())));
            film.setGenres(new HashSet<>(filmsGenres.getOrDefault(id, Collections.emptyList())));
            film.setDirectors(new HashSet<>(filmsDirectors.getOrDefault(id, Collections.emptyList())));
        }
    }

    private Map<Long, List<Genre>> getGenres(Map<Long, List<Long>> filmsGenresIds) {
        return filmsGenresIds.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(genreDao::getGenreById)
                                .map(Optional::get)
                                .collect(Collectors.toList())
                ));
    }

    private Map<Long, List<Director>> getDirectors(Map<Long, List<Long>> filmDirectorsIds) {
        return filmDirectorsIds.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(directorsDao::getDirectorById)
                                .map(Optional::get)
                                .collect(Collectors.toList())
                ));
    }

    private void sortFilmsByPopularityOrder(List<Film> films, List<Long> popularFilmIds) {
        films.sort(Comparator.comparingInt(film -> popularFilmIds.indexOf(film.getId())));
    }

    private void sortFilmsByYear(List<Film> films) {
        films.sort(Comparator.comparing(Film::getReleaseDate));
    }

    private void sortFilmsByLikes(List<Film> films) {
        films.sort(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed());
    }
}