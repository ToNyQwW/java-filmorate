package ru.yandex.practicum.filmorate.dao.filmGenre;

import ru.yandex.practicum.filmorate.entity.FilmGenres;

import java.util.List;
import java.util.Map;

public interface FilmGenreDao {

    FilmGenres getFilmGenres(Long filmId);

    Map<Long, FilmGenres> getFilmsGenresByListFilmIds(List<Long> filmIds);
}