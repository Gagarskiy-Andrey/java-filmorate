package ru.yandex.practicum.filmorate.mappers;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class FilmMapper {
    public Film mapToFilm(ResultSet resultSet) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getDate("releaseDate").toLocalDate());
        film.setDuration(resultSet.getInt("duration"));
        film.setMpa(new Mpa(resultSet.getInt("rating_id"), resultSet.getString("rating_name")));
        return film;
    }

    public Set<Genre> mapToGenres(ResultSet resultSet) throws SQLException {
        Set<Genre> genres = new LinkedHashSet<>();
        while (resultSet.next()) {
            Genre genre = new Genre(resultSet.getInt("genre_id"), resultSet.getString("name"));
            genres.add(genre);
        }
        return genres;
    }
}
