package ru.yandex.practicum.filmorate.dao.directors;

import ru.yandex.practicum.filmorate.entity.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorsDao {

    Director addDirector(Director director);

    List<Director> getAllDirectors();

    Optional<Director> getDirectorById(Long id);

    Director updateDirector(Director director);

    void deleteDirectorById(Long id);
}
