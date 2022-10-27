package ru.yandex.practicum.filmorate.DAOs.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAOs.GenreDao;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_GENRES_FOR_FILM = "SELECT g.genre_id, g.name " +
            "FROM genres g JOIN genres_films gf ON g.genre_id=gf.genre_id WHERE gf.films_id = ?";

    private static final String SQL_GET_GENRES = "SELECT genre_id, name FROM genres";

    private static final String SQL_GET_GENRE = "SELECT genre_id, name FROM genres WHERE genre_id=? ";

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> getGenreById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_GENRE, (rs, rowNum) -> makeGenre(rs), id));
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("Genre with id " + id + " not exists in the DB");
        }
    }

    @Override
    public List<Genre> findGenresByFilmId(Long id) {
        try {
            return jdbcTemplate.query(SQL_GET_GENRES_FOR_FILM, (rs, rowNum) -> makeGenre(rs), id);
        } catch (EmptyResultDataAccessException exception) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Genre> getGenres() {
        try {
            return jdbcTemplate.query(SQL_GET_GENRES, (rs, rowNum) -> makeGenre(rs));
        } catch (RuntimeException e) {
            throw new BadRequestException("Something went wrong.");
        }
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getLong("genre_id"), rs.getString("NAME"));
    }
}
