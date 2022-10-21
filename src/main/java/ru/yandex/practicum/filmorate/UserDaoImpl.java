package ru.yandex.practicum.filmorate;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findUserById(Long user_id) {
        String sql = "SELECT * from USERS where user_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUser(rs), user_id));
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User with id " + user_id + " not exists in the DB");
        }
    }

    @Override
    public Optional<User> add(User user) {
        String sql = "SELECT count(user_id) FROM users where email = ?";
        Integer count = this.jdbcTemplate.queryForObject(
                sql, Integer.class, new Object[]{ user.getEmail()});
        if (count != 0) {
            throw new AlreadyExistException("User with email " + user.getEmail() + " already exists in the DB");
        }
        sql = "INSERT INTO USERS (NAME, EMAIL, BIRTHDAY, LOGIN) VALUES(?, ?, ?, ?)";
        int id = jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getBirthday(), user.getLogin());
        if (id == 1) {
            sql = "SELECT TOP 1 * from USERS order by user_id DESC";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUser(rs)));
        } else {
            throw new NotExistException("User was not added in the DB");
        }
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = new User();
        Long id = rs.getLong("USER_ID");
        String name = rs.getString("NAME");
        String login = rs.getString("LOGIN");
        String email = rs.getString("EMAIL");
        LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setLogin(login);
        user.setBirthday(birthday);

        return user;
    }


}
