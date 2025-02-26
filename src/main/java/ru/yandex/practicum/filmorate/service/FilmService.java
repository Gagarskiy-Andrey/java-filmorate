package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final LikesStorage likeStorage;

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public Optional<Genre> getGenreById(int id) {
        return genreStorage.getGenreById(id);
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    public Optional<Mpa> getMpaById(int id) {
        return mpaStorage.getMpaById(id);
    }

    public Film save(Film film) {
        return filmStorage.save(film);
    }

    public Film getFilm(long filmId) {
        return filmStorage.get(filmId);
    }

    public List<Film> getFilmsList() {
        return filmStorage.getFilmsList();
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.get(filmId);
        userStorage.getUserById(userId);
        likeStorage.addLike(filmStorage.get(filmId), userStorage.getUserById(userId));
    }

    public void removeLike(Long filmId, Long userId) {
        filmStorage.get(filmId);
        userStorage.getUserById(userId);
        likeStorage.deleteLike(filmStorage.get(filmId), userStorage.getUserById(userId));
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}
