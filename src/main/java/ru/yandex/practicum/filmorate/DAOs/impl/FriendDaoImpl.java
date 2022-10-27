package ru.yandex.practicum.filmorate.DAOs.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAOs.FriendDao;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.Friend;
import ru.yandex.practicum.filmorate.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class FriendDaoImpl implements FriendDao {

    private static final String SQL_INSERT_FRIEND = "INSERT INTO users_users(users_id, friend_id, status_id) VALUES (?,?, 2)";
    private static final String SQL_GET_FRIEND = "SELECT * FROM users_users WHERE users_id=? AND friend_id=?";
    private static final String SQL_CONFIRM_FRIEND = "UPDATE users_users SET status_id =1 where users_id=? AND friend_id=?";
    private static final String SQL_REMOVE_FRIEND = "DELETE FROM users_users WHERE users_id=? AND friend_id=?";

    private final JdbcTemplate jdbcTemplate;


    public FriendDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        return null;
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        try {
            jdbcTemplate.update(SQL_INSERT_FRIEND, userId, friendId);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("User with id " + userId + " is already friend with user with id " + friendId);
        }
    }

    @Override
    public void confirmFriend(Long userId, Long friendId) {
        try {
            jdbcTemplate.update(SQL_CONFIRM_FRIEND, userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User with id " + friendId + "is not a friend for " + userId);
        }
    }

    @Override
    public Optional<Friend> getFriend(Long userId, Long friendId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_FRIEND, (rs, rowNum) -> makeFriend(rs), userId, friendId));
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User with id " + friendId + "is not a friend for " + userId);
        }
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        try {
            jdbcTemplate.update(SQL_REMOVE_FRIEND, userId, friendId);
        } catch (RuntimeException e) {
            throw new BadRequestException("Something went wrong.");
        }
    }

    public Friend makeFriend(ResultSet rs) throws SQLException {
        return new Friend(
                rs.getLong("USERS_ID"),
                rs.getLong("FRIEND_ID"),
                rs.getInt("STATUS_ID"));
    }
}
