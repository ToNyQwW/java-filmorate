package ru.yandex.practicum.filmorate.validation.validUserId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Slf4j
public class UserIdValidator implements ConstraintValidator<ValidUserId, Integer> {

    @Autowired
    private UserStorage userStorage;

    @Override
    public boolean isValid(Integer UserId, ConstraintValidatorContext context) {
        boolean exists = userStorage.containsUserId(UserId);

        if (!exists) {
            log.info("UserId not found: {}", UserId);
        }
        return exists;
    }
}