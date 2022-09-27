package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.storages.FilmStorage;
import ru.yandex.practicum.filmorate.storages.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public boolean setLike(long filmId, long userId) {
        if (!isInputDataValid(filmId, userId)) {
            Film film = filmStorage.getFilms().get(filmId);
            if (!film.getLikeUsers().contains(userId)) {
                film.getLikeUsers().add(userId);
                return true;
            } else {
                throw new AlreadyExistException("User with id " + userId + " already like film with id " + filmId);
            }
        }
        return false;
    }

    public boolean removeLike(long filmId, long userId) {
        if (isInputDataValid(filmId, userId)) {
            Film film = filmStorage.getFilms().get(filmId);
            if (film.getLikeUsers().contains(userId)) {
                film.getLikeUsers().remove(userId);
                return true;
            } else {
                throw new AlreadyExistException("User with id " + userId + " not like film with id " + filmId);
            }
        }
        return false;
    }

    public List<Film> getMostLikedFilms(int count) {
        List<Film> films = new ArrayList<>(filmStorage.getFilms().values());
        return films.stream()
                .sorted(Comparator.comparingInt(film -> film.getLikeUsers().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    private boolean isInputDataValid(long filmId, long userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new NotExistException("Film with specified id " + filmId + " is not exist");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotExistException("User with specified id " + userId + " is not exist");
        }
        return true;
    }


    public Film getById(long id) {
      return filmStorage.getById(id);
    }

    public List<Film> getAll() {
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    public Film add(Film film) {
        validate(film);
        return filmStorage.add(film);
    }


    public Film update(Film film) {
        validate(film);
       return filmStorage.update(film);
    }

    public Film remove(long id) {
      return filmStorage.remove(id);
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE)) {
        //    log.warn("В запросе передана невалидная дата релиза фильма -  {}", film.getReleaseDate());
            throw new ValidationException("Release date can be less than 28/12/1895");
        }
    }
}
