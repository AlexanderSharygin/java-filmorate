package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
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
    private static final String SQL_GET_FRIENDS_FOR_USER = "SELECT * FROM users WHERE user_id IN" +
            " (SELECT friend_id FROM users_users WHERE users_id=?)";
    private static final String SQL_GET_COMMON_FRIENDS = "SELECT * FROM users u WHERE user_id IN " +
            "(SELECT friend_id  FROM users_users WHERE users_id = ? OR users_id  = ? " +
            "GROUP BY friend_id HAVING COUNT (friend_id) = 2)";

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> users = jdbcTemplate.query(SQL_GET_USER, (rs, rowNum) -> makeUser(rs), id);

        if (users.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(users.get(0));
    }

    @Override
    public void add(User user) {
        jdbcTemplate.update(SQL_ADD_USER, user.getName(), user.getEmail(), user.getBirthday(), user.getLogin());
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(SQL_UPDATE_USER, user.getName(), user.getEmail(), user.getBirthday(), user.getLogin(), user.getId());
    }

    @Override
    public Optional<User> findNew() {
        List<User> users = jdbcTemplate.query(SQL_GET_NEWEST_USER, (rs, rowNum) -> makeUser(rs));
        if (users.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(users.get(0));
    }

    @Override
    public Optional<List<User>> findAll() {
        List<User> users = jdbcTemplate.query(SQL_GET_ALL_USERS, (rs, rowNum) -> makeUser(rs));

        return Optional.of(users);
    }

    @Override
    public Optional<List<User>> findFriends(Long userId) {
        List<User> users = jdbcTemplate.query(SQL_GET_FRIENDS_FOR_USER, (rs, rowNum) -> makeUser(rs), userId);

        return Optional.of(users);
    }

    @Override
    public Optional<List<User>> findCommonFriends(Long firstUserId, Long secondUserId) {
        List<User> users = jdbcTemplate.query(SQL_GET_COMMON_FRIENDS, (rs, rowNum) -> makeUser(rs), firstUserId, secondUserId);

        return Optional.of(users);
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