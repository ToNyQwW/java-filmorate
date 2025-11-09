package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.model.SortType;

import java.util.List;

public interface FilmService {

    Film addFilm(Film film);

    Film getFilm(Long id);

    List<Film> getFilms();

    List<Film> getFilmsByDirectorId(Long id, SortType sortType);

    Film updateFilm(Film film);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<Film> getPopularFilms(Long count);
}