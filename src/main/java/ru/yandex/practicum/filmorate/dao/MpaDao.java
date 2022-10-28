package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.models.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDao {
    Optional<Mpa> findById(Long id);

    Optional<List<Mpa>> findAll();
}
