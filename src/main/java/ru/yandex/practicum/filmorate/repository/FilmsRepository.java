package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilmsRepository {
    private final Map<Long, Film> filmsStorage = new HashMap<>();
    protected Long id = 1L;

    public Film save(Film film) {
        film.setId(getNextId());
        filmsStorage.put(film.getId(), film);
        return film;
    }

    public List<Film> getFilmsList() {
        return filmsStorage.values().stream().toList();
    }

    private Long getNextId() {
        return id++;
    }

    public Film findAndUpdateFilmById(Film film) {
        if (!filmsStorage.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с указанным id отсутствует");
        }
        filmsStorage.put(film.getId(), film);
        return film;
    }
}
