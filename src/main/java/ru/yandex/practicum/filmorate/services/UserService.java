package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getUsers() {
        return userDao.findAll();
    }

    public User getUserById(Long id) {
        return userDao
                .findById(id)
                .orElseThrow(() -> new NotExistException("User with id " + id + " not exists in the DB"));
    }

    public User addUser(User user) {
        checkName(user);
        try {
            userDao.add(user);
        } catch (DuplicateKeyException e) {
            throw new AlreadyExistException("User already exists in the DB");
        }
        return userDao
                .findNew()
                .orElseThrow(() -> new BadRequestException("Something went wrong."));
    }

    public User updateUser(User user) {
        checkName(user);
        try {
            userDao.update(user);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("User not exists in the DB");
        }
        return userDao
                .findById(user.getId())
                .orElseThrow(() -> new NotExistException("User with id" + user.getId() + " not exists in the DB"));
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("В запросе не передано Имя пользователя email {}. В качестве имени будет использован login  {}",
                    user.getEmail(), user.getLogin());
            user.setName(user.getLogin());
        }
    }
}