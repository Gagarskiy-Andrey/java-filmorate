package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.Add;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidFilm() {
        Film film = new Film();
        film.setId(1L);
        film.setDescription("Description");
        film.setDuration(120);
        film.setName("Test Film");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Add.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidName() {
        Film film = new Film();
        film.setId(1L);
        film.setDescription("Description");
        film.setDuration(120);
        film.setName(" ");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Add.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidDescription() {
        Film film = new Film();
        film.setId(1L);
        film.setDescription("Description Description Description Description Description Description " +
                "Description Description Description Description Description Description Description " +
                "Description Description Description Description");
        film.setDuration(120);
        film.setName("Test Film");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Add.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidReleaseDate() {
        Film film = new Film();
        film.setId(1L);
        film.setDescription("Description");
        film.setDuration(120);
        film.setName("Test Film");
        film.setReleaseDate(LocalDate.of(1800, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Add.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testInvalidDuration() {
        Film film = new Film();
        film.setId(1L);
        film.setDescription("Description");
        film.setDuration(-120);
        film.setName("Test Film");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film, Add.class);
        assertFalse(violations.isEmpty());
    }
}
