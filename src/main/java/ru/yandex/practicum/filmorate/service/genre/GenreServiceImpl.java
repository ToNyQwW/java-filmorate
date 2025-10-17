package ru.yandex.practicum.filmorate.service.genre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Override
    public List<Genre> getAllGenres() {
        var result = genreDao.getAllGenres();
        log.info("Number of Genres found: {}", result.size());
        return result;
    }

    @Override
    public Genre getGenreById(Long id) {
        var genre = genreDao.getGenreById(id);

        if (genre.isEmpty()) {
            log.error("Genre with id {} not found", id);
            throw new NotFoundException("Genre with id " + id + " not found");
        }
        log.info("Genre found: {}", genre);
        return genre.get();
    }
}
