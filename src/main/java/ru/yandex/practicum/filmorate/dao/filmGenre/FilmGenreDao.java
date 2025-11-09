package ru.yandex.practicum.filmorate.dao.filmGenre;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmGenreDao {

    void addFilmGenres(Long filmId, Set<Genre> genres);

    List<Long> getFilmGenres(Long filmId);

    Map<Long, List<Long>> getFilmsGenresByListFilmIds(List<Long> filmIds);
}