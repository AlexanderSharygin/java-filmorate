package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        return userService.add(user);
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping(value = "/users/{id}")
    public User removerUser(@PathVariable("id") Integer userId) {
        return userService.remove(userId);
    }

    @GetMapping("/users/{id}")
    public User getFilmById(@PathVariable("id") Integer usersId) {
        return userService.getUserById(usersId);
    }
}