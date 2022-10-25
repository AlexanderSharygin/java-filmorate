package ru.yandex.practicum.filmorate.storages;

import java.util.List;

public interface Storage<T> {

    List<T> getAll();

    T getById(long id);

    T add(T value);

    T update(T value);

    boolean isExist(long id);
}
