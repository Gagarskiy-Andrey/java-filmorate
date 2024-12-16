package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmsRepository;
import ru.yandex.practicum.filmorate.validators.Add;
import ru.yandex.practicum.filmorate.validators.Update;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    FilmsRepository filmsRepository = new FilmsRepository();

    @GetMapping
    public Collection<Film> getAll() {
        return filmsRepository.get().values();
    }

    @PostMapping
    public Film addFilm(@Validated(Add.class) @RequestBody Film film) {
        return filmsRepository.save(film);
    }

    @PutMapping
    public Film updateFilm(@Validated(Update.class) @RequestBody Film film) {
        if (!filmsRepository.get().containsKey(film.getId())) {
            throw new NotFoundException("Фильм с указанным id отсутствует");
        }
        filmsRepository.get().put(film.getId(), film);
        return film;
    }
}