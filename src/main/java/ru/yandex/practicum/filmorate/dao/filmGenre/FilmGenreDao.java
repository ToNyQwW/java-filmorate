package ru.yandex.practicum.filmorate.dao.filmGenre;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmGenreDao {

    void addFilmGenres(Long filmId, Set<Genre> genres);

    List<Genre> getFilmGenres(Long filmId);

    Map<Long, List<Genre>> getFilmsGenresByListFilmIds(List<Long> filmIds);
}