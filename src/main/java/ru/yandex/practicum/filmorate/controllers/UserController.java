package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.UserIsNotExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {


    private final List<User> users = new ArrayList<>();
    private int idCounter = 0;

    @GetMapping("/users")
    public List<User> getAll() {
        return users;
    }


    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        if (users.stream()
                .anyMatch(k -> k.getEmail().equals(user.getEmail()))) {
            throw new UserAlreadyExistException("User account is already exist.");
        }
        user.setId(idCounter);
        users.add(user);
        idCounter++;
        return user;
    }

    @PutMapping(value = "/users")
    public Optional<User> update(@RequestBody User user) {
        Optional<User> existedUser = users.stream()
                .filter(k -> k.getEmail().equals(user.getEmail()))
                .findFirst();
        if (existedUser.isEmpty()) {
            throw new UserIsNotExistException("User with specified email is not find.");
        } else {
            existedUser.get().setEmail(user.getEmail());
            existedUser.get().setLogin(user.getLogin());
            existedUser.get().setName(user.getName());
            existedUser.get().setBirthday(user.getBirthday());
            return existedUser;
        }
    }
}
