package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@Slf4j
public class UserController {

    private final List<User> users = new ArrayList<>();
    private long idCounter = 1;

    @GetMapping("/users")
    public ResponseEntity<?> getAll() {
        log.info("Текущее количество пользователей: {}", users.size());
        return ResponseEntity.ok(users);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<?> create(@RequestBody User user) {
        validate(user);
        checkName(user);
        if (users.stream()
                .anyMatch(k -> k.getEmail().equals(user.getEmail()))) {
            log.error("Аккаунт пользователя с email {} уже существует", user.getEmail());
            throw new AlreadyExistException("User account is already exist.");
        }
        user.setId(idCounter);
        users.add(user);
        idCounter++;
        log.info("Добавлен пользователь с email {}", user.getEmail());
        return ResponseEntity.ok(user);
    }

    @PutMapping(value = "/users")
    public ResponseEntity<?> update(@RequestBody User user) {
        validate(user);
        checkName(user);
        Optional<User> existedUser = users.stream()
                .filter(k -> k.getId().equals(user.getId()))
                .findFirst();
        if (existedUser.isEmpty()) {
            log.error("Аккаунт пользователя с id {} не существует", user.getId());
            throw new NotExistException("User with specified email is not find.");
        } else {
            existedUser.get().setEmail(user.getEmail());
            existedUser.get().setLogin(user.getLogin());
            existedUser.get().setName(user.getName());
            existedUser.get().setBirthday(user.getBirthday());
            log.info("Обновлен пользователь с email {}", user.getEmail());
            return ResponseEntity.ok(existedUser.get());
        }
    }

    private void validate(User user) {
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(user.getLogin());

        if (user.getEmail().isBlank()) {
            log.warn("В запросе передано пустой email");
            throw new ValidationException("Email can't be empty");
        }

        if (!user.getEmail().contains("@")) {
            log.warn("В запросе передано невалидный email -  {}", user.getEmail());
            throw new ValidationException("Email should contain @ char");
        }
        if (user.getLogin().isBlank()) {
            log.warn("В запросе передан пустой login");
            throw new ValidationException("Login can't be empty");
        }
        if (matcher.find()) {
            log.warn("В запросе передано невалидный login -  {}", user.getLogin());
            throw new ValidationException("Login can't contain whitespaces");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("В запросе передано невалидная дата рождения -  {}", user.getBirthday());
            throw new ValidationException("Birthday can be in the future");
        }
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("В запросе не передано Имя пользователя email {}. В качестве имени будет использован login  {}",
                    user.getEmail(), user.getLogin());
            user.setName(user.getLogin());
        }
    }
}