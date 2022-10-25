package ru.yandex.practicum.filmorate.DAOs.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAOs.UserDao;
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
public class UserDaoImpl implements UserDao {

    private static final String SQL_GET_USER = "SELECT * FROM users WHERE user_id = ?";
    private static final String SQL_GET_ALL_USERS = "SELECT * FROM users";
    private static final String SQL_GET_NEWEST_USER = "SELECT TOP 1 * FROM users ORDER BY user_id DESC";
    private static final String SQL_ADD_USER = "INSERT INTO users (name, email, birthday, login) VALUES(?, ?, ?, ?)";
    private static final String SQL_UPDATE_USER = "UPDATE users SET name =?, email=?, birthday=?, login=? WHERE user_id=?";
    private static final String SQL_INSERT_FRIEND = "INSERT INTO users_users(users_id, friend_id, status_id) VALUES (?,?, 2)";
    private static final String SQL_GET_FRIEND = "SELECT * FROM users_users WHERE users_id=? AND friend_id=?";
    private static final String SQL_CONFIRM_FRIEND = "UPDATE users_users SET status_id =1 where users_id=? AND friend_id=?";
    private static final String SQL_REMOVE_FRIEND = "DELETE FROM users_users WHERE users_id=? AND friend_id=?";
    private static final String SQL_GET_FRIENDS_FOR_USER = "SELECT * FROM users WHERE user_id IN" +
            " (SELECT friend_id FROM users_users WHERE users_id=?)";
    private static final String SQL_GET_COMMON_FRIENDS = "SELECT * FROM users u WHERE user_id IN " +
            "(SELECT friend_id  FROM users_users WHERE users_id = ? OR users_id  = ? " +
            "GROUP BY friend_id HAVING count (friend_id) = 2)";

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> getUserById(Long user_id) {
        try {

            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_USER, (rs, rowNum) -> makeUser(rs), user_id));
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User with id " + user_id + " not exists in the DB");
        }
    }

    @Override
    public Optional<User> addUser(User user) {
        try {
            jdbcTemplate.update(SQL_ADD_USER, user.getName(), user.getEmail(), user.getBirthday(), user.getLogin());

            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_NEWEST_USER, (rs, rowNum) -> makeUser(rs)));

        } catch (DuplicateKeyException e) {
            throw new AlreadyExistException("User already exists in the DB");
        }
    }

    @Override
    public Optional<User> updateUser(User user) {
        try {
            jdbcTemplate.update(SQL_UPDATE_USER, user.getName(), user.getEmail(), user.getBirthday(), user.getLogin(), user.getId());

            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_USER, (rs, rowNum) -> makeUser(rs), user.getId()));

        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User with id " + user.getId() + " not exists in the DB");
        }
    }

    @Override
    public List<User> getUsers() {
        try {

            return jdbcTemplate.query(SQL_GET_ALL_USERS, (rs, rowNum) -> makeUser(rs));
        } catch (RuntimeException e) {
            throw new BadRequestException("Something went wrong.");
        }
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        try {
            jdbcTemplate.queryForObject(SQL_GET_USER, (rs, rowNum) -> makeUser(rs), userId);
            jdbcTemplate.queryForObject(SQL_GET_USER, (rs, rowNum) -> makeUser(rs), friendId);
            jdbcTemplate.update(SQL_INSERT_FRIEND, userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User or film not exists in the DB");
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("User with id " + userId + " is already friend with user with id " + friendId);
        }
    }

    @Override
    public void confirmFriend(Long userId, Long friendId) {
        try {
            jdbcTemplate.queryForObject(SQL_GET_FRIEND, (rs, rowNum) -> makeFriend(rs), userId, friendId);
            jdbcTemplate.update(SQL_CONFIRM_FRIEND, userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User with id " + friendId + "is not a friend for " + userId);
        }
    }

    @Override
    public void removeFriend(Long userId, Long friendId) {
        try {
            jdbcTemplate.queryForObject(SQL_GET_FRIEND, (rs, rowNum) -> makeFriend(rs), userId, friendId);
            jdbcTemplate.update(SQL_REMOVE_FRIEND, userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User with id " + friendId + "is not a friend for " + userId);
        }
    }

    @Override
    public List<User> getFriendsForUser(Long userId) {
        try {

            return jdbcTemplate.query(SQL_GET_FRIENDS_FOR_USER, (rs, rowNum) -> makeUser(rs), userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("Were are not friends for user with id " + userId);
        }
    }

    @Override
    public List<User> getCommonFriends(Long firstUserId, Long secondUserId) {
        try {

            return jdbcTemplate.query(SQL_GET_COMMON_FRIENDS, (rs, rowNum) -> makeUser(rs), firstUserId, secondUserId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("Were are not common friends for user with id " + firstUserId + " and " + secondUserId);
        }
    }

    public Friend makeFriend(ResultSet rs) throws SQLException {
        return new Friend(
                rs.getLong("USERS_ID"),
                rs.getLong("FRIEND_ID"),
                rs.getInt("STATUS_ID"));
    }

    public User makeUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("USER_ID"),
                rs.getString("NAME"),
                rs.getString("LOGIN"),
                rs.getString("EMAIL"),
                rs.getDate("BIRTHDAY").toLocalDate());
    }
}