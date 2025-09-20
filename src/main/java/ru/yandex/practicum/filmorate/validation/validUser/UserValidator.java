package ru.yandex.practicum.filmorate.validation.validUser;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Slf4j
@Component
public class UserValidator implements ConstraintValidator<ValidUser, User> {

    @Autowired
    private UserStorage userStorage;

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        boolean exists = userStorage.containsUserId(user.getId());

        if (!exists) {
            log.info("User not found: {}", user);
            throw new UserNotFoundException(user.getId());
        }
        return exists;
    }
}