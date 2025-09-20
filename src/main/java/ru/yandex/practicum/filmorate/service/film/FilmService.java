package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.validFilm.ValidFilm;
import ru.yandex.practicum.filmorate.validation.validFilmId.ValidFilmId;
import ru.yandex.practicum.filmorate.validation.validUserId.ValidUserId;

import java.util.List;

public interface FilmService {

    Film addFilm(Film film);

    Film getFilm(@ValidFilmId Long id);

    List<Film> getFilms();

    Film updateFilm(@ValidFilm Film film);

    void addLike(@ValidFilmId Long filmId, @ValidUserId Long userId);

    void removeLike(@ValidFilmId Long filmId, @ValidUserId Long userId);

    List<Film> getPopularFilms(Long count);
}
