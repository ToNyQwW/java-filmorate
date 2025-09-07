package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeAll
    static void beforeAll() {
        factory.close();
    }

    private Film createFilm() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(Duration.ofMinutes(200));
        return film;
    }

    @Test
    void testEmptyFilm() {
        Film film = createFilm();
        film.setName("");
        film.setDescription("");
        film.setReleaseDate(null);
        film.setDuration(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(4, violations.size());
    }

    @Test
    void testBlankName() {
        Film film = createFilm();
        film.setName("");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("{jakarta.validation.constraints.NotBlank.message}",
                violations.iterator().next().getMessageTemplate());
    }

    @Test
    void testDescriptionTooLong() {
        Film film = createFilm();
        film.setDescription("A".repeat(201));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("{jakarta.validation.constraints.Size.message}", violations.iterator().next().getMessageTemplate());
    }

    @Test
    void testReleaseDateBeforeMin() {
        Film film = createFilm();
        film.setReleaseDate(LocalDate.of(1800, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Release date cannot be earlier than December 28, 1895",
                violations.iterator().next().getMessage());
    }

    @Test
    void testNegativeDuration() {
        Film film = createFilm();
        film.setDuration(Duration.ofMinutes(-5));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals("Duration must be positive",
                violations.iterator().next().getMessage());
    }

    @Test
    void testZeroDuration() {
        Film film = createFilm();
        film.setDuration(Duration.ZERO);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals("Duration must be positive",
                violations.iterator().next().getMessage());
    }
}
