package ru.yandex.practicum.filmorate.service.mpa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.entity.Mpa;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MpaServiceImpl implements MpaService {

    private final MpaDao mpaDao;

    @Override
    public List<Mpa> getAllMpa() {
        var result = mpaDao.getAllMpa();
        log.info("Number of Mpa found: {}", result.size());
        return result;
    }

    @Override
    public Mpa getMpaById(Long id) {
        var mpa = mpaDao.getMpaById(id);

        if (mpa.isEmpty()) {
            log.error("Mpa with id {} not found", id);
            throw new NotFoundException("Mpa with id " + id + " not found");
        }
        log.info("Mpa found: {}", mpa);
        return mpa.get();
    }
}