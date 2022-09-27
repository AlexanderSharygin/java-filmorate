package ru.yandex.practicum.filmorate.storages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Long, User> users = new HashMap<>();
    private long idCounter = 1;

    @Override
    public HashMap<Long, User> getUsers() {
        if (users.isEmpty()) {
            throw new NotExistException("Users list is empty.");
        }
        log.info("Текущее количество пользователей: {}", users.size());
        return users;
    }

    @Override
    public User getById(long id) {
        Optional<User> user = Optional.ofNullable(users.get(id));
        if (user.isEmpty()) {
            throw new NotExistException("User with id" + id + "was not find.");
        } else {
            return user.get();
        }
    }

    @Override
    public User add(User user) {
        if (users.values().stream()
                .anyMatch(k -> k.getEmail().equals(user.getEmail()))) {
            throw new AlreadyExistException("User account with email " + user.getEmail() + " is already exist.");
        }
        user.setId(idCounter);
        users.put(idCounter, user);
        idCounter++;
        log.info("Добавлен пользователь с email {}", user.getEmail());
        return user;
    }

    @Override
    public User update(User user) {
        Optional<User> existedUser = users.values().stream()
                .filter(k -> k.getId().equals(user.getId()))
                .findFirst();
        if (existedUser.isEmpty()) {
            throw new NotExistException("User with specified id " + user.getId() + " is not find.");
        } else {
            existedUser.get().setEmail(user.getEmail());
            existedUser.get().setLogin(user.getLogin());
            existedUser.get().setName(user.getName());
            existedUser.get().setBirthday(user.getBirthday());
            log.info("Обновлен пользователь с email {}", user.getEmail());
            return existedUser.get();
        }
    }

    @Override
    public User remove(long id) {
        Optional<User> user = Optional.ofNullable(users.get(id));
        if (user.isEmpty()) {
            throw new NotExistException("User with id" + id + "was not found.");
        } else {
            users.remove(id);
            log.info("Удален пользователь {}.", user.get().getEmail());
            return user.get();
        }
    }
}
