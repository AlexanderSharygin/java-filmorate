package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreFilmDao;

@Component
public class GenreFilmDaoImpl implements GenreFilmDao {

    private static final String SQL_INSERT_GENRE = "INSERT INTO GENRES_FILMS (genre_id, films_id) VALUES (?,?)";

    private static final String SQL_DELETE_GENRES_FOR_FILM = "DELETE FROM genres_films WHERE films_id=?";
    private final JdbcTemplate jdbcTemplate;

    public GenreFilmDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addGenreForFilm(Long filmId, Long genreId) {
        jdbcTemplate.update(SQL_INSERT_GENRE, genreId, filmId);
    }

    @Override
    public void removeGenreForFilm(Long filmId) {
        jdbcTemplate.update(SQL_DELETE_GENRES_FOR_FILM, filmId);
    }
}