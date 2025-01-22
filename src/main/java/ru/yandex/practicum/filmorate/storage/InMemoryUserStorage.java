package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> usersStorage = new HashMap<>();
    private final Map<Long, Set<User>> friendsStorage = new HashMap<>();
    protected Long id = 1L;

    @Override
    public User save(User user) {
        user.setId(getNextId());
        setLoginAsName(user);
        usersStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getUsersList() {
        return usersStorage.values().stream().toList();
    }

    @Override
    public User update(User user) {
        if (!usersStorage.containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с указанным id отсутствует");
        }
        setLoginAsName(user);
        usersStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new RuntimeException("Нельзя добавить самого себя в друзья!");
        }
        if (!friendsStorage.containsKey(userId)) {
            if (!friendsStorage.containsKey(friendId)) {
                addFirstFriend(friendId, usersStorage.get(userId));
            } else {
                friendsStorage.get(friendId).add(usersStorage.get(userId));
            }
            addFirstFriend(userId, usersStorage.get(friendId));
        } else {
            friendsStorage.get(userId).add(usersStorage.get(friendId));
        }
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        if (friendsStorage.containsKey(userId)) {
            if (!friendsStorage.get(userId).contains(usersStorage.get(friendId))) {
                throw new NotFoundException("Пользователь с id " + friendId + " не найден среди друзей пользователя с id " + userId);
            }
            if (friendsStorage.containsKey(friendId)) {
                if (!friendsStorage.get(friendId).contains(usersStorage.get(userId))) {
                    throw new NotFoundException("Пользователь с id " + userId + " не найден среди друзей пользователя с id " + friendId);
                }
                friendsStorage.get(friendId).remove(usersStorage.get(userId));
            }
            friendsStorage.get(userId).remove(usersStorage.get(friendId));
        }
    }

    @Override
    public List<User> getFriends(Long userId) {
        if (!friendsStorage.containsKey(userId) || friendsStorage.get(userId).isEmpty()) {
            return new ArrayList<>();
            //throw new NotFoundException("У пользователя с id " + userId + " нет друзей");
        }
        return new ArrayList<>(friendsStorage.get(userId));
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long anotherUserId) {
        if (!friendsStorage.containsKey(userId) || friendsStorage.get(userId).isEmpty()) {
            throw new NotFoundException("У пользователя с id " + userId + " нет друзей");
        }
        if (!friendsStorage.containsKey(anotherUserId) || friendsStorage.get(anotherUserId).isEmpty()) {
            throw new NotFoundException("У пользователя с id " + anotherUserId + " нет друзей");
        }
        return friendsStorage.get(anotherUserId).stream()
                .filter(friend -> friendsStorage.get(userId).contains(friend)).toList();
    }

    @Override
    public User getForCheck(Long id) {
        Optional<User> checkUser = Optional.ofNullable(usersStorage.get(id));
        final User user = checkUser.orElseThrow(() -> new NotFoundException("Не найден пользователь с id" + id));
        return user;
    }

    private Long getNextId() {
        return id++;
    }

    private void setLoginAsName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void addFirstFriend(Long userId, User friend) {
        Set<User> friends = new HashSet<>();
        friends.add(friend);
        friendsStorage.put(userId, friends);
    }
}