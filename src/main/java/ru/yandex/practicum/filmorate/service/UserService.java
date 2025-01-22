package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User save(User user) {
        return userStorage.save(user);
    }

    public List<User> getUsersList() {
        return userStorage.getUsersList();
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public void addFriend(Long userId, Long friendId) {
        checkAvailability(userId);
        checkAvailability(friendId);
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {
        checkAvailability(userId);
        checkAvailability(friendId);
        userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(Long userId) {
        checkAvailability(userId);
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long anotherUserId) {
        checkAvailability(userId);
        checkAvailability(anotherUserId);
        return userStorage.getCommonFriends(userId, anotherUserId);
    }

    public User checkAvailability(Long id) {
        return userStorage.getForCheck(id);
    }
}
