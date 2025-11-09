package ru.yandex.practicum.filmorate.dao.filmDirectors;

import ru.yandex.practicum.filmorate.entity.Director;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmDirectorsDao {

    void addFilmDirector(Long filmId, Set<Director> directors);

    List<Long> getFilmDirectors(Long filmId);

    List<Long> getFilmsByDirectorId(Long directorId);

    Map<Long, List<Long>> getFilmsDirectorsByListFilmIds(List<Long> filmIds);
}
