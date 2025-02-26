package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Slf4j
@Primary
@Repository
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private static final String INSERT_QUERY = "INSERT INTO users (email, login, name, birth_day) " +
            "VALUES (?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
                userMapper.setUserParameters(ps, user);
                return ps;
            }, keyHolder);

            long userId = keyHolder.getKey().longValue();
            user.setId(userId);
            return user;
        } catch (Exception e) {
            log.error("Неизвестная ошибка при добавлении пользователя: ", e);
            throw new RuntimeException("Неизвестная ошибка при добавлении пользователя", e);
        }
    }

    @Override
    public List<User> getUsersList() {
        final String GET_USERS_QUERY = "SELECT * FROM users";
        return jdbcTemplate.query(GET_USERS_QUERY, userMapper::mapToUser);
    }

    @Override
    public User getUserById(Long id) {
        final String GET_USER_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(GET_USER_BY_ID_QUERY, userMapper::mapToUser, id);
        } catch (Exception e) {
            log.error("Пользователь с id={} не найден", id);
            throw new NotFoundException("Пользователь с указанным id не найден");
        }
    }

    @Override
    public User update(User newUser) {
        final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birth_day = ? WHERE user_id = ?";
        int rowsUpdated = jdbcTemplate.update(UPDATE_USER_QUERY,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                Date.valueOf(newUser.getBirthday()),
                newUser.getId()
        );
        if (rowsUpdated == 0) {
            log.error("Не удалось обновить пользователя с id={}", newUser.getId());
            throw new NotFoundException("Пользователь с указанным id не найден");
        }
        return getUserById(newUser.getId());
    }

}
