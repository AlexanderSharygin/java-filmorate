package ru.yandex.practicum.filmorate.storages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    @Override
    public HashMap<Long, Film> getFilms() {
        if (films.isEmpty()) {
            throw new NotExistException("Films list is empty.");
        }
        log.info("Текущее количество фильмов: {}", films.size());
        return films;
    }

    private final HashMap<Long, Film> films = new HashMap<>();
    private long idCounter = 1;

    @Override
    public Film getById(long id) {
        Optional<Film> film = Optional.ofNullable(films.get(id));
        if (film.isEmpty()) {
            throw new NotExistException("Film with id" + id + "was not find.");
        } else {
            return film.get();
        }
    }

    @Override
    public Film add(Film film) {
        if (films.values().stream()
                .anyMatch(k -> k.getName().equals(film.getName()) &&
                        k.getReleaseDate().equals(film.getReleaseDate()))) {
            throw new AlreadyExistException("Film with name " + film.getName() + " and release date " +
                    film.getReleaseDate() + " already exists in the DB.");
        }
        film.setId(idCounter);
        films.put(idCounter, film);
        idCounter++;
        log.info("Добавлен фильм  {} с датой {}", film.getName(), film.getReleaseDate());
        return film;
    }

    @Override
    public Film update(Film film) {
        Optional<Film> existedFilm = Optional.ofNullable(films.get(film.getId()));
        if (existedFilm.isEmpty()) {
            throw new NotExistException("Film with id" + film.getId() + "was not found.");
        } else {
            existedFilm.get().setReleaseDate(film.getReleaseDate());
            existedFilm.get().setDescription(film.getDescription());
            existedFilm.get().setName(film.getName());
            existedFilm.get().setDuration(film.getDuration());
            log.info("Обновлен фильм {} с датой {}. Новое название {} и дата {}", film.getName(), film.getReleaseDate(),
                    existedFilm.get().getName(), existedFilm.get().getReleaseDate());
            return existedFilm.get();
        }
    }

    @Override
    public Film remove(long id) {
        Optional<Film> existedFilm = Optional.ofNullable(films.get(id));
        if (existedFilm.isEmpty()) {
            throw new NotExistException("Film with id" + id + "was not found.");
        } else {
            films.remove(id);
            log.info("Удален фильм {} с датой {}.", existedFilm.get().getName(), existedFilm.get().getReleaseDate());
            return existedFilm.get();
        }
    }
}
