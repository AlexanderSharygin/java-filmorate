package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.models.Film;

import java.util.HashMap;
import java.util.List;

public interface FilmStorage {

    HashMap<Long,Film> getFilms();

    Film getById(long id);

    Film add(Film film);

    Film update(Film film);

    Film remove(long id);

}
