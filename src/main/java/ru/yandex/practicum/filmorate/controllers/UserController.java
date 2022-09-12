package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.UserIsNotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {

    private final List<User> users = new ArrayList<>();
    private int idCounter = 1;

    @GetMapping("/users")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(users);
    }


    @PostMapping(value = "/users")
    public ResponseEntity<?> create(@RequestBody User user) {
        validate(user);
        if (users.stream()
                .anyMatch(k -> k.getEmail().equals(user.getEmail()))) {
            throw new UserAlreadyExistException("User account is already exist.");
        }
        user.setId(idCounter);
        users.add(user);
        idCounter++;
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/users")
    public ResponseEntity<?> update(@RequestBody User user) {
        validate(user);
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
            return ResponseEntity.ok(existedUser);
        }
    }

    private void validate(User user) {
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(user.getLogin());

        if (user.getEmail().isBlank()) {
            throw new ValidationException("Email can't be empty");
        }

        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email should contain @ char");
        }
        if (user.getLogin().isBlank()) {
            throw new ValidationException("Login can't be empty");
        }
        if (matcher.find()) {
            throw new ValidationException("Login can't contain whitespaces");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday can be in the future");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
