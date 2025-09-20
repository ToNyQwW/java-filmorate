package ru.yandex.practicum.filmorate.validation.validFilmId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Slf4j
public class FilmIdValidator implements ConstraintValidator<ValidFilmId, Long> {

    @Autowired
    private FilmStorage filmStorage;

    @Override
    public boolean isValid(Long filmId, ConstraintValidatorContext context) {
        boolean exists = filmStorage.containsId(filmId);

        if (!exists) {
            log.info("FilmId not found: {}", filmId);
            throw new NotFoundException("Film with id=" + filmId + " not found");
        }
        return exists;
    }
}