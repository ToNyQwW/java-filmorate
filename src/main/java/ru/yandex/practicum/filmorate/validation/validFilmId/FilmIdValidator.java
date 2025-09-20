package ru.yandex.practicum.filmorate.validation.validFilmId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Slf4j
public class FilmIdValidator implements ConstraintValidator<ValidFilmId, Integer> {

    @Autowired
    private FilmStorage filmStorage;

    @Override
    public boolean isValid(Integer filmId, ConstraintValidatorContext context) {
        boolean exists = filmStorage.containsId(filmId);

        if (!exists) {
            log.info("FilmId not found: {}", filmId);
        }
        return exists;
    }
}