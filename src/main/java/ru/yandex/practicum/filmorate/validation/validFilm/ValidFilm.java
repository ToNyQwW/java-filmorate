package ru.yandex.practicum.filmorate.validation.validFilm;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilmValidator.class)
public @interface ValidFilm {

    String message() default "Film not found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
