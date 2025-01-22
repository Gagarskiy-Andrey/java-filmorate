package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validators.Add;
import ru.yandex.practicum.filmorate.validators.Update;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getAll() {
        log.info("Get запрос /films");
        List<Film> allFilms = filmService.getFilmsList();
        log.info("Ответ Get /films с телом: {}", allFilms);
        return allFilms;
    }

    @PostMapping
    public Film addFilm(@Validated(Add.class) @RequestBody Film film) {
        log.info("Post запрос /films с телом: {}", film);
        Film savedFilm = filmService.save(film);
        log.info("Ответ Post /films с телом: {}", savedFilm);
        return savedFilm;
    }

    @PutMapping
    public Film updateFilm(@Validated(Update.class) @RequestBody Film film) {
        log.info("Put запрос /films с телом: {}", film);
        Film updatedFilm = filmService.update(film);
        log.info("Ответ Put /films с телом: {}", updatedFilm);
        return updatedFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Put запрос на добавление лайка фильму с Id {} пользователем с Id = {}", id, userId);
        filmService.addLike(id, userId);
        log.debug("Фильму с Id {} получил лайк от пользователя с Id = {}", id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Delete запрос на удаление лайка фильму с Id {} пользователем с Id = {}", id, userId);
        filmService.removeLike(id, userId);
        log.debug("Фильм с Id {} лишился лайка от пользователя с Id = {}", id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Get запрос /films на получение {} популярных фильмов", count);
        List<Film> popularFilms = filmService.getPopularFilms(count);
        log.info("Ответ Get /films на получение {} популярных фильмов с телом: {}", count, popularFilms);
        return popularFilms;
    }
}
