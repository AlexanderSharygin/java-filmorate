package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.models.User;

import java.util.HashMap;
import java.util.List;

public interface UserStorage {

    HashMap<Long, User> getUsers();

    User getById(long id);

    User add(User user);

    User update(User user);

    User remove(long id);
}
