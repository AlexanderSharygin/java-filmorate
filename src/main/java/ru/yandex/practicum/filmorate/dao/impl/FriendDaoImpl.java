package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.models.Friend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class FriendDaoImpl implements FriendDao {

    private static final String SQL_INSERT_FRIEND = "INSERT INTO users_users(users_id, friend_id, status_id) VALUES (?,?, 2)";
    private static final String SQL_GET_FRIEND = "SELECT * FROM users_users WHERE users_id=? AND friend_id=?";
    private static final String SQL_CONFIRM_FRIEND = "UPDATE users_users SET status_id =1 WHERE users_id=? AND friend_id=?";
    private static final String SQL_REMOVE_FRIEND = "DELETE FROM users_users WHERE users_id=? AND friend_id=?";

    private final JdbcTemplate jdbcTemplate;


    public FriendDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void add(Long userId, Long friendId) {
        jdbcTemplate.update(SQL_INSERT_FRIEND, userId, friendId);
    }

    @Override
    public void confirm(Long userId, Long friendId) {
        jdbcTemplate.update(SQL_CONFIRM_FRIEND, userId, friendId);
    }

    @Override
    public Optional<Friend> find(Long userId, Long friendId) {
        List<Friend> friends = jdbcTemplate.query(SQL_GET_FRIEND, (rs, rowNum) -> makeFriend(rs), userId, friendId);

        if (friends.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(friends.get(0));
    }

    @Override
    public void remove(Long userId, Long friendId) {
        jdbcTemplate.update(SQL_REMOVE_FRIEND, userId, friendId);
    }

    public Friend makeFriend(ResultSet rs) throws SQLException {
        return new Friend(
                rs.getLong("USERS_ID"),
                rs.getLong("FRIEND_ID"),
                rs.getInt("STATUS_ID"));
    }
}