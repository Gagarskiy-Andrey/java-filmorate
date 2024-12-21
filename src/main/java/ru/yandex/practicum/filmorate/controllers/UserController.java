package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UsersRepository;
import ru.yandex.practicum.filmorate.validators.Add;
import ru.yandex.practicum.filmorate.validators.Update;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    UsersRepository usersRepository = new UsersRepository();

    @GetMapping
    public List<User> getAll() {
        return usersRepository.getUsersList();
    }

    @PostMapping
    public User addUser(@Validated(Add.class) @RequestBody User user) {
        log.info("Post запрос /users с телом: {}", user);
        ifNameIsEmpty(user);
        User savedUser = usersRepository.save(user);
        log.info("Ответ Post /users с телом: {}", savedUser);
        return savedUser;
    }

    @PutMapping
    public User updateUser(@Validated(Update.class) @RequestBody User user) {
        log.info("Put запрос /users с телом: {}", user);
        ifNameIsEmpty(user);
        User updatedUser = usersRepository.findAndUpdateUserById(user);
        log.info("Ответ Put /users с телом: {}", updatedUser);
        return updatedUser;
    }

    void ifNameIsEmpty(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
