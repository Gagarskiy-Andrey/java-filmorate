package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmMapper.class})
public class JdbcFilmStorageTest {
    private final FilmDbStorage filmStorage;

    @BeforeEach
    public void setUp() {
        filmStorage.save(new Film(null, "Test Film 1", "testFilm", LocalDate.of(1966,6,6), 120, new Mpa(3, "PG-13")));
        filmStorage.save(new Film(null, "Test Film 2", "testFilm", LocalDate.of(1968,8,8), 120, new Mpa(3, "PG-13")));
    }

    @Test
    public void testAddFilm() {
        Film newFilm = new Film(null, "Test Film 3", "testFilm",
                LocalDate.of(1970,10,10), 120, new Mpa(3, "PG-13"));

        Film addedFilm = filmStorage.save(newFilm);

        assertThat(addedFilm).isNotNull();
        assertThat(addedFilm.getId()).isNotNull();
        assertThat(addedFilm.getName()).isEqualTo("Test Film 3");
        assertThat(addedFilm.getDescription()).isEqualTo("testFilm");
        assertThat(addedFilm.getReleaseDate()).isEqualTo(LocalDate.of(1970,10,10));
        assertThat(addedFilm.getDuration()).isEqualTo(120);
        assertThat(addedFilm.getMpa().getName()).isEqualTo("PG-13");
    }

    @Test
    public void testGetUsers() {
        Collection<Film> films = filmStorage.getFilmsList();

        assertThat(films).isNotEmpty();
        assertThat(films).anyMatch(film -> film.getId() != null && film.getName() != null);
    }

    @Test
    public void testUpdateFilm_NotFound() {
        Long nonExistentFilmId = 999L;
        Film nonExistentFilm = new Film(nonExistentFilmId, "Non Existent Film", "nonexistentfilm",
                LocalDate.of(1967,12,9), 120, new Mpa(3, "PG-13"));

        assertThatThrownBy(() -> filmStorage.update(nonExistentFilm))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Фильм не найден. Ошибка обнавления");
    }
}
