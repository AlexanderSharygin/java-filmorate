package ru.yandex.practicum.filmorate.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<List<T>> findAll();

    Optional<T> findById(Long id);

    void add(T value);

    void update(T value);
}
