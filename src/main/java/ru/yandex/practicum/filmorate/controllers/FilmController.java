package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmsRepository;
import ru.yandex.practicum.filmorate.validators.Add;
import ru.yandex.practicum.filmorate.validators.Update;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmsRepository filmsRepository = new FilmsRepository();

    @GetMapping
    public List<Film> getAll() {
        log.info("Get запрос /films");
        List<Film> allFilms = filmsRepository.getFilmsList();
        log.info("Ответ Get /films с телом: {}", allFilms);
        return allFilms;
    }

    @PostMapping
    public Film addFilm(@Validated(Add.class) @RequestBody Film film) {
        log.info("Post запрос /films с телом: {}", film);
        Film savedFilm = filmsRepository.save(film);
        log.info("Ответ Post /films с телом: {}", savedFilm);
        return savedFilm;
    }

    @PutMapping
    public Film updateFilm(@Validated(Update.class) @RequestBody Film film) {
        log.info("Put запрос /films с телом: {}", film);
        Film updatedFilm = filmsRepository.update(film);
        log.info("Ответ Put /films с телом: {}", updatedFilm);
        return updatedFilm;
    }
}
