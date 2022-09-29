package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.HashMap;

public interface FilmStorage {

    HashMap<Long, Film> getAll();

    Film getById(long id);

    Film add(Film film);

    Film update(Film film);
}
