package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public User save(User user);

    public List<User> getUsersList();

    public User update(User user);

    public void addFriend(Long userId, Long friendId);

    public void removeFriend(Long userId, Long friendId);

    public List<User> getFriends(Long userId);

    public List<User> getCommonFriends(Long userId, Long anotherUserId);

    public User getForCheck(Long id);
}
