package ru.yandex.practicum.filmorate.validation.validFilm;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

@Slf4j
public class FilmValidator implements ConstraintValidator<ValidFilm, Film> {

    @Autowired
    private FilmStorage filmStorage;

    @Override
    public boolean isValid(Film film, ConstraintValidatorContext context) {
        boolean exists = filmStorage.containsId(film.getId());

        if (!exists) {
            log.info("Film not found: {}", film);
        }
        return exists;
    }
}
