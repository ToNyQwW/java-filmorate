package ru.yandex.practicum.filmorate.service.directors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.directors.DirectorsDao;
import ru.yandex.practicum.filmorate.entity.Director;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DirectorsServiceImpl implements DirectorsService {

    private final DirectorsDao directorsDao;

    @Override
    public Director addDirector(Director director) {

        var addedDirector = directorsDao.addDirector(director);
        log.info("director added: {}", addedDirector);
        return addedDirector;
    }

    @Override
    public List<Director> getAllDirectors() {
        var directors = directorsDao.getAllDirectors();
        log.info("Number of directors found: {}", directors.size());
        return directors;
    }

    @Override
    public Director getDirectorById(Long id) {
        var director = directorsDao.getDirectorById(id);

        if (director.isPresent()) {
            var findedDirector = director.get();
            log.info("director found: {}", findedDirector);
            return findedDirector;
        }
        log.error("director not found");
        throw new NotFoundException("director not found");
    }

    @Override
    public Director updateDirector(Director director) {
        var updatedDirector = directorsDao.updateDirector(director);
        log.info("Director updated: {}", updatedDirector);

        return updatedDirector;
    }

    @Override
    public void deleteDirectorById(Long id) {
        directorsDao.deleteDirectorById(id);
        log.info("Director with id deleted: {}", id);
    }
}