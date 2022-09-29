package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.models.User;

import java.util.HashMap;

public interface UserStorage {

    HashMap<Long, User> getAll();

    User getById(long id);

    User add(User user);

    User update(User user);
}
