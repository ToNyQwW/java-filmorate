package ru.yandex.practicum.filmorate.service.directors;

import ru.yandex.practicum.filmorate.entity.Director;

import java.util.List;

public interface DirectorsService {

    Director addDirector(Director director);

    List<Director> getAllDirectors();

    Director getDirectorById(Long id);

    Director updateDirector(Director director);

    void deleteDirectorById(Long id);
}
