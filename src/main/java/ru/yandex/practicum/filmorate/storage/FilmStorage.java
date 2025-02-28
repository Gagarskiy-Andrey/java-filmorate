package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film save(Film film);

    List<Film> getFilmsList();

    Film update(Film film);

    List<Film> getPopularFilms(int count);

    Film get(Long filmId);
}
