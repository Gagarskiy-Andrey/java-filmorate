package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validators.Add;
import ru.yandex.practicum.filmorate.validators.Update;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        log.info("Get запрос /users");
        List<User> allUsers = userService.getUsersList();
        log.info("Ответ Get /users с телом: {}", allUsers);
        return allUsers;
    }

    @PostMapping
    public User addUser(@Validated(Add.class) @RequestBody User user) {
        log.info("Post запрос /users с телом: {}", user);
        User savedUser = userService.save(user);
        log.info("Ответ Post /users с телом: {}", savedUser);
        return savedUser;
    }

    @PutMapping
    public User updateUser(@Validated(Update.class) @RequestBody User user) {
        log.info("Put запрос /users с телом: {}", user);
        User updatedUser = userService.update(user);
        log.info("Ответ Put /users с телом: {}", updatedUser);
        return updatedUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Put запрос на добавление друга с id {} пользователю с id {}", friendId, id);
        userService.addFriend(id, friendId);
        log.debug("Пользователь с id {} и пользователь с id {} теперь друзья.", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Delete запрос на удаление друга с id {} пользователем с id {}", friendId, id);
        userService.removeFriend(id, friendId);
        log.debug("Пользователь с id {} и пользователь с id {} больше не являются друзьями.", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("Get запрос на получение всех друзей пользователя с id {}", id);
        List<User> allFriend = userService.getFriends(id);
        log.info("Ответ Get /{}/friends с телом: {}", id, allFriend);
        return allFriend;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id,
                                       @PathVariable Long otherId) {
        log.info("Get запрос на получение общих друзей пользователя с id {}, а так же пользователя с id {} ", id, otherId);
        List<User> commonFriends = userService.getCommonFriends(id, otherId);
        log.info("Ответ Get /{}/friends/common/{} с телом: {}", id, otherId, commonFriends);
        return commonFriends;
    }
}
