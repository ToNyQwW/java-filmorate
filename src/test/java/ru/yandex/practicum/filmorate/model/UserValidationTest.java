package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @BeforeAll
    static void beforeAll() {
        factory.close();
    }

    private User createUser() {
        User user = new User();
        user.setName("user");
        user.setEmail("email@email");
        user.setLogin("login");
        user.setBirthday(LocalDate.now());
        return user;
    }

    @Test
    void testValidUser() {
        User user = createUser();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidEmail() {
        User user = createUser();
        user.setEmail("invalid-email");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("{jakarta.validation.constraints.Email.message}",
                violations.iterator().next().getMessageTemplate());
    }

    @Test
    void testBlankLogin() {
        User user = createUser();
        user.setLogin("   ");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("{jakarta.validation.constraints.Pattern.message}",
                violations.iterator().next().getMessageTemplate());
    }

    @Test
    void testFutureBirthday() {
        User user = createUser();
        user.setBirthday(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("{jakarta.validation.constraints.PastOrPresent.message}",
                violations.iterator().next().getMessageTemplate());
    }
}