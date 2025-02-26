package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public User save(User user);

    public List<User> getUsersList();

    public User update(User user);

    public User getUserById(Long id);
}
