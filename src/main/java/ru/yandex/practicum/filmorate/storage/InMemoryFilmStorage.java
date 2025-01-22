package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> filmsStorage = new HashMap<>();
    private final Map<Film, Set<Long>> likesFilms = new HashMap<>();
    protected Long id = 1L;

    @Override
    public Film save(Film film) {
        film.setId(getNextId());
        filmsStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getFilmsList() {
        return filmsStorage.values().stream().toList();
    }

    @Override
    public Film update(Film film) {
        if (!filmsStorage.containsKey(film.getId())) {
            throw new NotFoundException("Фильм с указанным id отсутствует");
        }
        filmsStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        if (!likesFilms.containsKey(filmsStorage.get(filmId))) {
            Set<Long> likes = new HashSet<>();
            likes.add(userId);
            likesFilms.put(filmsStorage.get(filmId), likes);
        } else {
            likesFilms.get(filmsStorage.get(filmId)).add(userId);
        }
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        if (likesFilms.containsKey(filmsStorage.get(filmId))) {
            likesFilms.get(filmsStorage.get(filmId)).remove(userId);
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return likesFilms.entrySet().stream()
                .sorted((film1, film2) -> (film2.getValue().size() - film1.getValue().size()))
                .map(Map.Entry::getKey)
                .limit(count)
                .toList();
    }

    @Override
    public Film get(Long filmId) {
        Optional<Film> checkFilm = Optional.ofNullable(filmsStorage.get(filmId));
        final Film film = checkFilm.orElseThrow(() -> new NotFoundException("Не найден фильм с id" + filmId));
        return film;
    }

    private Long getNextId() {
        return id++;
    }
}
