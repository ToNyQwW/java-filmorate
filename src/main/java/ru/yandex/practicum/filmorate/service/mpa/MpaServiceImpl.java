package ru.yandex.practicum.filmorate.service.mpa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.entity.Mpa;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MpaServiceImpl implements MpaService {

    private final MpaDao mpaDao;

    @Override
    public List<Mpa> getAllMpa() {
        return mpaDao.getAllMpa();
    }

    @Override
    public Mpa getMpaById(Long id) {
        return mpaDao.getMpaById(id);
    }
}