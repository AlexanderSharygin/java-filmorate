package ru.yandex.practicum.filmorate.storages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class InMemoryFilmStorage implements Storage<Film> {

    private final HashMap<Long, Film> films = new HashMap<>();
    private long idCounter = 1;

    @Override
    public List<Film> getAll() {
        log.info("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getById(long id) {
        Optional<Film> film = Optional.ofNullable(films.get(id));
        if (film.isEmpty()) {
            throw new NotExistException("Film with id" + id + "was not find.");
        } else {
            log.info("Найден фильм с id: {}", film.get().getId());

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
        }
        existedFilm.get().setReleaseDate(film.getReleaseDate());
        existedFilm.get().setDescription(film.getDescription());
        existedFilm.get().setName(film.getName());
        existedFilm.get().setDuration(film.getDuration());
        log.info("Обновлен фильм {} с датой {}. Новое название {} и дата {}", film.getName(), film.getReleaseDate(),
                existedFilm.get().getName(), existedFilm.get().getReleaseDate());

        return existedFilm.get();
        
    }

    @Override
    public boolean isExist(long id) {
        return films.containsKey(id);
    }
}