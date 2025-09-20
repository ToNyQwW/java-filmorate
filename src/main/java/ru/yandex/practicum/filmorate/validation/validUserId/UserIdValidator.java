package ru.yandex.practicum.filmorate.validation.validUserId;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Slf4j
@Component
public class UserIdValidator implements ConstraintValidator<ValidUserId, Long> {

    @Autowired
    private UserStorage userStorage;

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext context) {
        boolean exists = userStorage.containsUserId(userId);

        if (!exists) {
            log.info("UserId not found: {}", userId);
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        return exists;
    }
}