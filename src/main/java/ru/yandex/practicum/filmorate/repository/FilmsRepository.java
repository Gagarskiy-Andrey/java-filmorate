package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

public class FilmsRepository {
    HashMap<Long, Film> filmsStorage = new HashMap<>();

    public Film save(Film film) {
        film.setId(getNextId());
        filmsStorage.put(film.getId(), film);
        return film;
    }

    public HashMap<Long, Film> get() {
        return filmsStorage;
    }

    private long getNextId() {
        long currentMaxId = filmsStorage.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
