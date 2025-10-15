package ru.yandex.practicum.filmorate.dao.filmGenre;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;
import java.util.Map;

public interface FilmGenreDao {

    List<Genre> getFilmGenres(Long filmId);

    Map<Long, List<Genre>> getFilmsGenresByListFilmIds(List<Long> filmIds);
}