package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public Film save(Film film) {
        return filmStorage.save(film);
    }

    public List<Film> getFilmsList() {
        return filmStorage.getFilmsList();
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.get(filmId);
        userStorage.getForCheck(userId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        filmStorage.get(filmId);
        userStorage.getForCheck(userId);
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}
