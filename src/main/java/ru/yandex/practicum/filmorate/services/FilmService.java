package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    @Value("${server.minDate}")
    private String minDate;


    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getMostPopularFilms(int count) {
        List<Film> films = new ArrayList<>(filmStorage.getAll().values());
        var sorted = films.stream().sorted((f1, f2) -> f2.getLikeUsers().size() - f1.getLikeUsers().size()).collect(Collectors.toList());
        return sorted.stream().limit(count)
                .collect(Collectors.toList());
    }

    public Film getById(long id) {
        return filmStorage.getById(id);
    }

    public List<Film> getAll() {
        return new ArrayList<>(filmStorage.getAll().values());
    }

    public Film addFilm(Film film) {
        validate(film);
        return filmStorage.add(film);
    }

    public boolean addLike(long filmId, long userId) {
        if (isInputDataValid(filmId, userId)) {
            Film film = filmStorage.getAll().get(filmId);
            if (!film.getLikeUsers().contains(userId)) {
                film.getLikeUsers().add(userId);
                log.info("Пользователь с id {} лайкнул фильм с id {}", userId, filmId);
                return true;
            } else {
                throw new AlreadyExistException("User with id " + userId + " already like film with id " + filmId);
            }
        }
        return false;
    }

    public Film updateFilm(Film film) {
        validate(film);
        return filmStorage.update(film);
    }

    public boolean removeLike(long filmId, long userId) {
        if (isInputDataValid(filmId, userId)) {
            Film film = filmStorage.getAll().get(filmId);
            if (film.getLikeUsers().contains(userId)) {
                film.getLikeUsers().remove(userId);
                log.info("Пользователь с id {} удалил лайк у фильма с id {}", userId, filmId);
                return true;
            } else {
                throw new AlreadyExistException("User with id " + userId + " not like film with id " + filmId);
            }
        }
        return false;
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.parse(minDate))) {
            throw new ValidationException("Release date can be less than 28/12/1895");
        }
    }

    private boolean isInputDataValid(long filmId, long userId) {
        if (!filmStorage.getAll().containsKey(filmId)) {
            throw new NotExistException("Film with specified id " + filmId + " is not exist");
        }
        if (!userStorage.getAll().containsKey(userId)) {
            throw new NotExistException("User with specified id " + userId + " is not exist");
        }
        return true;
    }
}