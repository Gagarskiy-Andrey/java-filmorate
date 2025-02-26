package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

@Slf4j
@Repository
@RequiredArgsConstructor
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbc;

    @Override
    public void addLike(Film film, User user) {
        String sql = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbc.update(sql, film.getId(), user.getId());
        log.info("Лайк добавлен фильму с id {} от пользователя с id {}", film.getId(), user.getId());
    }

    @Override
    public void deleteLike(Film film, User user) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbc.update(sql, film.getId(), user.getId());
        log.info("Лайк удален у фильма с id {} от пользователя с id {}", film.getId(), user.getId());
    }
}
