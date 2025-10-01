package ru.yandex.practicum.filmorate.validation.minReleaseDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class MinReleaseDateValidator implements ConstraintValidator<MinReleaseDate, LocalDate> {

    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate == null) {
            return true;
        }
        var checkDate = !localDate.isBefore(MIN_DATE);
        if (!checkDate) {
            log.error("Incorrect date");
        }
        return checkDate;
    }
}