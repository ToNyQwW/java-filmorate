package ru.yandex.practicum.filmorate.dao.mpa;

import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;

public interface MpaDao {

    List<Mpa> getAllMpa();

    Mpa getMpaById(Long id);
}