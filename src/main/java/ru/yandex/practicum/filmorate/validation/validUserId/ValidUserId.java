package ru.yandex.practicum.filmorate.validation.validUserId;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserIdValidator.class)
public @interface ValidUserId {

    String message() default "User not found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
