package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.entity.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreDao {

    List<Genre> getAllGenres();

    Optional<Genre> getGenreById(Long id);

    List<Genre> getGenresByListId(List<Long> ids);
}
