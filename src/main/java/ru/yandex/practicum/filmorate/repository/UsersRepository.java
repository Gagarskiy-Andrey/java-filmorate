package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersRepository {
    private final Map<Long, User> usersStorage = new HashMap<>();
    protected Long id = 1L;

    public User save(User user) {
        user.setId(getNextId());
        usersStorage.put(user.getId(), user);
        return user;
    }

    public List<User> getUsersList() {
        return usersStorage.values().stream().toList();
    }

    public User update(User user) {
        if (!usersStorage.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с указанным id отсутствует");
        }
        usersStorage.put(user.getId(), user);
        return user;
    }

    private Long getNextId() {
        return id++;
    }
}