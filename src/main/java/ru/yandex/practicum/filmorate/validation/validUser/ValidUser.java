package ru.yandex.practicum.filmorate.validation.validUser;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserValidator.class)
public @interface ValidUser {

    String message() default "User not found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
