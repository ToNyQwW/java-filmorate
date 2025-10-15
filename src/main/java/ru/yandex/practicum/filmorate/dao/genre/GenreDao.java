package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;

public interface GenreDao {

    List<Genre> getAllGenres();

    Genre getGenreById(Long id);

    List<Genre> getGenresByListId(List<Long> ids);
}
