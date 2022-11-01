package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    public Optional<Genre> findById(Long id) {
        List<Genre> genres = jdbcTemplate.query(SQL_GET_GENRE, (rs, rowNum) -> makeGenre(rs), id);

        if (genres.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(genres.get(0));
    }

    @Override
    public List<Genre> findByFilmId(Long id) {

        return jdbcTemplate.query(SQL_GET_GENRES_FOR_FILM, (rs, rowNum) -> makeGenre(rs), id);
    }

    @Override
    public List<Genre> findAll() {
        List<Genre> genres = jdbcTemplate.query(SQL_GET_GENRES, (rs, rowNum) -> makeGenre(rs));

        return genres;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getLong("genre_id"), rs.getString("NAME"));
    }
}
