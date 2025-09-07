package ru.yandex.practicum.filmorate.Validation.positiveDuration;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PositiveDurationValidator.class)
public @interface PositiveDuration {

    String message() default "Duration must be positive";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
