package ru.yandex.practicum.filmorate.DAOs.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAOs.LikeDao;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.Like;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class LikeDaoImpl implements LikeDao {

    private static final String SQL_INSERT_LIKE = "INSERT into likes (user_id, film_id) VALUES (?,?)";

    private static final String SQL_GET_LIKE = "SELECT * FROM likes WHERE user_id=? AND film_id=?";

    private static final String SQL_DELETE_LIKE = "DELETE FROM likes WHERE user_id=? AND film_id=?";
    private final JdbcTemplate jdbcTemplate;

    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Like> getLIke(Long filmId, Long userId) {
        try {
            return  Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_LIKE, (rs, rowNum) -> makeLike(rs), userId, filmId));
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User with id " + userId + " is not like film with id " + filmId);
        }
    }

    @Override
    public void addLIke(Long filmId, Long userId) {
        try {
            jdbcTemplate.update(SQL_INSERT_LIKE, userId, filmId);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("User with id " + userId + " is already like film with id " + filmId);
        }
    }
    private Like makeLike(ResultSet rs) throws SQLException {
        return new Like(rs.getLong("user_id"), rs.getLong("film_id"));
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        try {
            jdbcTemplate.update(SQL_DELETE_LIKE, userId, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User with id " + userId + " is not like film with id " + filmId);
        }
    }
}
