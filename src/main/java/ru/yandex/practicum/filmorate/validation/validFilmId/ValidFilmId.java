package ru.yandex.practicum.filmorate.validation.validFilmId;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FilmIdValidator.class)
public @interface ValidFilmId {

    String message() default "FilmId not found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
