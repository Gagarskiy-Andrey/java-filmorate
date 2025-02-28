package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDbStorage;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaDbStorage.class})
public class JdbcMpaStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    public void getRatingById() {
        Mpa testMpa = new Mpa(2, "PG");

        Optional<Mpa> userOptional = mpaDbStorage.getMpaById(2);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 2)
                )
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(testMpa));
    }

    @Test
    public void getAllRatings() {
        List<Mpa> testMpaList = List.of(new Mpa(1, "G"), new Mpa(2, "PG"),
                new Mpa(3, "PG-13"), new Mpa(4, "R"), new Mpa(5, "NC-17"));

        Optional<List<Mpa>> userOptional = Optional.ofNullable(mpaDbStorage.getAllMpa());

        assertThat(userOptional)
                .isPresent()
                .usingRecursiveComparison()
                .isEqualTo(Optional.of(testMpaList));

    }
}
