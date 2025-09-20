package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public class FilmServiceImpl implements FilmService {
    @Override
    public Film addFilm(Film film) {
        return null;
    }

    @Override
    public Film getFilm(int id) {
        return null;
    }

    @Override
    public List<Film> getFilms() {
        return List.of();
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public void addLike(int filmId, int userId) {

    }

    @Override
    public void removeLike(int filmId, int userId) {

    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return List.of();
    }
}
