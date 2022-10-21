package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

  /*  @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAll();
    }*/



    @GetMapping("/users/{id}")
    public Optional<User> getUserById(@PathVariable("id") Long usersId) {
        return userService.findUserById(usersId);
    }

    @PostMapping(value = "/users")
    public Optional<User> create(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

   /* @GetMapping(value = "/users/{id}/friends")
    public List<User> getUserFriends(@PathVariable long id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") long id, @PathVariable("otherId") long friendId) {
        return userService.getCommonFriends(id, friendId);
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public boolean addFriend(@PathVariable("id") long userId, @PathVariable long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public boolean removeFriend(@PathVariable("id") long userId, @PathVariable long friendId) {
        return userService.removeFriend(userId, friendId);
    }*/
}