package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

public class UsersRepository {
    HashMap<Long, User> usersStorage = new HashMap<>();

    public User save(User user) {
        user.setId(getNextId());
        usersStorage.put(user.getId(), user);
        return user;
    }

    public HashMap<Long, User> get() {
        return usersStorage;
    }

    private long getNextId() {
        long currentMaxId = usersStorage.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
