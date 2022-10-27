package ru.yandex.practicum.filmorate.DAOs.impl;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAOs.FilmsGenresDao;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;

@Component
public class FilmGenreImpl implements FilmsGenresDao {

    private static final String SQL_INSERT_GENRE = "insert into GENRES_FILMS (genre_id, films_id) values (?,?)";

    private static final String SQL_DELETE_GENRES_FOR_FILM = "DELETE FROM genres_films WHERE films_id=?";
    private final JdbcTemplate jdbcTemplate;

    public FilmGenreImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addGenreForFilm(Long filmId, Long genreId) {
        try {
            jdbcTemplate.update(SQL_INSERT_GENRE, genreId, filmId);
        } catch (DuplicateKeyException e) {
            throw new AlreadyExistException("Film with genre already exists in the DB");

        } catch (RuntimeException e) {
            throw new BadRequestException("Something went wrong.");
        }
    }

    @Override
    public void removeGenreForFilm(Long filmId) {
        try {
            jdbcTemplate.update(SQL_DELETE_GENRES_FOR_FILM, filmId);
        } catch (RuntimeException e) {
            throw new BadRequestException("Something went wrong.");
        }
    }

}
