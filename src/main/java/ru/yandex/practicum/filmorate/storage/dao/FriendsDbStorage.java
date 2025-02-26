package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class FriendsDbStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public void addFriend(Long userId, Long friendId) {
        final String CHECK_USER_QUERY = "SELECT COUNT(*) FROM users WHERE user_id = ?";
        final String CHECK_QUERY = "SELECT COUNT(*) FROM friendship WHERE (user_id = ? AND friend_id = ?) " +
                "OR (user_id = ? AND friend_id = ?)";
        final String INSERT_QUERY = "INSERT INTO friendship (user_id, friend_id, status) VALUES (?, ?, ?)";
        final String UPDATE_QUERY = "UPDATE friendship SET status = TRUE WHERE user_id = ? AND friend_id = ?";
        final String UPDATE_REVERSE_QUERY = "UPDATE friendship SET status = FALSE WHERE user_id = ? AND friend_id = ?";

        try {
            Integer userCount = jdbcTemplate.queryForObject(CHECK_USER_QUERY, Integer.class, userId);
            if (userCount == null || userCount == 0) {
                throw new NotFoundException("Пользователь с ID " + userId + " не найден");
            }

            Integer friendCount = jdbcTemplate.queryForObject(CHECK_USER_QUERY, Integer.class, friendId);
            if (friendCount == null || friendCount == 0) {
                throw new NotFoundException("Пользователь с ID " + friendId + " не найден");
            }

            int count = jdbcTemplate.queryForObject(CHECK_QUERY, Integer.class, userId, friendId, friendId, userId);

            if (count > 0) {
                return;
            }

            jdbcTemplate.update(INSERT_QUERY, userId, friendId, false);

            count = jdbcTemplate.queryForObject(CHECK_QUERY, Integer.class, friendId, userId, userId, friendId);

            if (count > 0) {
                jdbcTemplate.update(UPDATE_QUERY, userId, friendId);
                jdbcTemplate.update(UPDATE_REVERSE_QUERY, friendId, userId);
            }
        } catch (NotFoundException e) {
            log.error("Ошибка: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Ошибка при добавлении друга: userId={}, friendId={}", userId, friendId, e);
            throw new RuntimeException("Ошибка при добавлении друга", e);
        }
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        final String DELETE_FRIEND_QUERY = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        final String CHECK_USER_QUERY = "SELECT COUNT(*) FROM users WHERE user_id = ?";

        try {
            Integer userCount = jdbcTemplate.queryForObject(CHECK_USER_QUERY, Integer.class, id);
            if (userCount == null || userCount == 0) {
                throw new NotFoundException("Пользователь с ID " + id + " не найден");
            }
            Integer friendCount = jdbcTemplate.queryForObject(CHECK_USER_QUERY, Integer.class, friendId);
            if (friendCount == null || friendCount == 0) {
                throw new NotFoundException("Пользователь с ID " + friendId + " не найден");
            }
            jdbcTemplate.update(DELETE_FRIEND_QUERY, id, friendId);
        } catch (NotFoundException e) {
            log.error("Ошибка: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<User> getFriends(Long id) {
        final String GET_FRIENDS_QUERY =
                "SELECT u.* FROM users u " +
                        "JOIN friendship f ON u.user_id = f.friend_id " +
                        "WHERE f.user_id = ?";
        final String CHECK_USER_QUERY = "SELECT COUNT(*) FROM users WHERE user_id = ?";

        try {
            Integer userCount = jdbcTemplate.queryForObject(CHECK_USER_QUERY, Integer.class, id);
            if (userCount == null || userCount == 0) {
                throw new NotFoundException("Пользователь с ID " + id + " не найден");
            }
            return jdbcTemplate.query(GET_FRIENDS_QUERY, userMapper::mapToUser, id);
        } catch (NotFoundException e) {
            log.error("Ошибка: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<User> getCommonFriends(Long id, Long friendId) {
        final String GET_COMMON_FRIENDS_QUERY =
                "SELECT u.* FROM users u " +
                        "JOIN friendship f1 ON u.user_id = f1.friend_id " +
                        "JOIN friendship f2 ON u.user_id = f2.friend_id " +
                        "WHERE f1.user_id = ? AND f2.user_id = ?";
        return jdbcTemplate.query(GET_COMMON_FRIENDS_QUERY, userMapper::mapToUser, id, friendId);
    }
}
