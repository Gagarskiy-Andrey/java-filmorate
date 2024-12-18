package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.Add;
import ru.yandex.practicum.filmorate.validators.Update;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user, Add.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidEmail() {
        User user = new User();
        user.setId(1L);
        user.setEmail("");
        user.setLogin("testuser");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user, Add.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidLogin() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("test user"); // содержит пробел
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<User>> violations = validator.validate(user, Add.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidBirthday() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setBirthday(LocalDate.now().plusDays(1)); // дата в будущем

        Set<ConstraintViolation<User>> violations = validator.validate(user, Add.class);
        assertFalse(violations.isEmpty());
    }


}
