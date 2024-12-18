package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UsersRepository;
import ru.yandex.practicum.filmorate.validators.Add;
import ru.yandex.practicum.filmorate.validators.Update;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    UsersRepository usersRepository = new UsersRepository();

    @GetMapping
    public Collection<User> getAll() {
        return usersRepository.get().values();
    }

    @PostMapping
    public User addUser(@Validated(Add.class) @RequestBody User user) {
        log.info("Создание нового пользователя: {}", user.getLogin());
        ifNameIsEmpty(user);
        User savedUser = usersRepository.save(user);
        log.info("Пользователь c id = {} успешно добавлен", savedUser.getId());
        return savedUser;
    }

    @PutMapping
    public User updateUser(@Validated(Update.class) @RequestBody User user) {
        if (!usersRepository.get().containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с указанным id отсутствует");
        }
        log.info("Обновление данных пользователя с id = {}", user.getId());
        ifNameIsEmpty(user);
        usersRepository.get().put(user.getId(), user);
        log.info("Пользователь с id = {} успешно обновлён", user.getId());
        return user;
    }

    void ifNameIsEmpty(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
