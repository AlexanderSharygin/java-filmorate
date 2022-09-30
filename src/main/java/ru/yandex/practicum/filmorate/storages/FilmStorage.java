package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getAll();

    Film getById(long id);

    Film add(Film film);

    Film update(Film film);

    boolean isContainValue(long id);
}
