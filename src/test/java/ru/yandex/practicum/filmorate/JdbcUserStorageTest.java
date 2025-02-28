package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserMapper.class})
class JdbcUserStorageTest {
    private final UserDbStorage userStorage;

    @BeforeEach
    public void setUp() {
        userStorage.save(new User(null, "test@example.com", "testuser",
                "Test User", LocalDate.of(1990, 1, 1), Collections.emptySet()));
        userStorage.save(new User(null, "test2@example.com", "testuser2", "Test User 2",
                LocalDate.of(1992, 2, 2), Collections.emptySet()));
    }

    @Test
    public void testAddUser() {
        User newUser = new User(null, "test11@example.com", "testlogin", "Test User",
                LocalDate.of(1990, 1, 1), new HashSet<>());

        User addedUser = userStorage.save(newUser);

        assertThat(addedUser).isNotNull();
        assertThat(addedUser.getId()).isNotNull();
        assertThat(addedUser.getEmail()).isEqualTo("test11@example.com");
        assertThat(addedUser.getLogin()).isEqualTo("testlogin");
        assertThat(addedUser.getName()).isEqualTo("Test User");
        assertThat(addedUser.getBirthday()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    public void testGetUsers() {
        Collection<User> users = userStorage.getUsersList();

        assertThat(users).isNotEmpty();
        assertThat(users).anyMatch(user -> user.getId() != null && user.getName() != null);
    }

    @Test
    public void testUpdateUser_NotFound() {
        Long nonExistentUserId = 999L;
        User nonExistentUser = new User(nonExistentUserId, "nonexistent@example.com",
                "nonexistentLogin", "Non Existent User", LocalDate.of(1995, 5, 5),
                new HashSet<>());

        assertThatThrownBy(() -> userStorage.update(nonExistentUser))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Пользователь с указанным id не найден");
    }
}
