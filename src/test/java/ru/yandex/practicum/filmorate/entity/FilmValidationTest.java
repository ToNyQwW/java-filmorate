package ru.yandex.practicum.filmorate.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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

    @AfterAll
    static void afterAll() {
        factory.close();
    }

    private Film createFilm() {
        return Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.now())
                .duration(200)
                .build();
    }

    @Test
    void testEmptyFilm() {
        Film film = createFilm();
        film.setName("");
        film.setDescription("");
        film.setReleaseDate(null);
        film.setDuration(0);
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
        assertEquals("{jakarta.validation.constraints.Size.message}",
                violations.iterator().next().getMessageTemplate());
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
        film.setDuration(-5);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals("{jakarta.validation.constraints.Positive.message}",
                violations.iterator().next().getMessageTemplate());
    }

    @Test
    void testZeroDuration() {
        Film film = createFilm();
        film.setDuration(0);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals("{jakarta.validation.constraints.Positive.message}",
                violations.iterator().next().getMessageTemplate());
    }
}