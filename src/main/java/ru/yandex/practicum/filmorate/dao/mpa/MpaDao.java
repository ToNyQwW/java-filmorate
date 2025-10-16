package ru.yandex.practicum.filmorate.dao.mpa;

import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDao {

    List<Mpa> getAllMpa();

    Optional<Mpa> getMpaById(Long id);
}