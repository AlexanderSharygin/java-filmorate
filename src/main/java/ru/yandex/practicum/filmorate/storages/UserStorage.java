package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.models.User;

import java.util.List;

public interface UserStorage {

    List<User> getAll();

    User getById(long id);

    User add(User user);

    User update(User user);

    boolean remove(User user);
}
