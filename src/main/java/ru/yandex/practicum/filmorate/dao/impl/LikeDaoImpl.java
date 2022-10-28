package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.LikeDao;
import ru.yandex.practicum.filmorate.models.Like;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class LikeDaoImpl implements LikeDao {

    private static final String SQL_INSERT_LIKE = "INSERT INTO likes (user_id, film_id) VALUES (?,?)";

    private static final String SQL_GET_LIKE = "SELECT * FROM likes WHERE user_id=? AND film_id=?";

    private static final String SQL_DELETE_LIKE = "DELETE FROM likes WHERE user_id=? AND film_id=?";
    private final JdbcTemplate jdbcTemplate;

    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Like> find(Long filmId, Long userId) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_LIKE, (rs, rowNum) -> makeLike(rs), userId, filmId));
    }

    @Override
    public void add(Long filmId, Long userId) {
        jdbcTemplate.update(SQL_INSERT_LIKE, userId, filmId);
    }

    private Like makeLike(ResultSet rs) throws SQLException {
        return new Like(rs.getLong("user_id"), rs.getLong("film_id"));
    }

    @Override
    public void remove(Long filmId, Long userId) {
        jdbcTemplate.update(SQL_DELETE_LIKE, userId, filmId);
    }
}