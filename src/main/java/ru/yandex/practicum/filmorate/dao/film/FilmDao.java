package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.entity.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {

    Film addFilm(Film film);

    Optional<Film> getFilm(Long id);

    List<Film> getFilms();

    List<Film> getPopularFilms(Long count);

    Film updateFilm(Film film);
}