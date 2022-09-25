package ru.yandex.practicum.filmorate.storages;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
    private final HashMap<Long, Film> films = new HashMap<>();
    private long idCounter = 1;

    @Override
    public List<Film> getAll() {
        if (films.isEmpty()) {
            throw new NotExistException("Films list is empty.");
        }
        return new ArrayList<>(films.values());
    }

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
            throw new AlreadyExistException("Film with name " + film.getName() + " and release date " + film.getReleaseDate() + " already exists in the DB.");
        }
        film.setId(idCounter);
        films.put(idCounter, film);
        idCounter++;
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
            return existedFilm.get();
        }
    }

    @Override
    public boolean remove(Film film) {
        Optional<Film> existedFilm = Optional.ofNullable(films.get(film.getId()));
        if (existedFilm.isEmpty()) {
            throw new NotExistException("Film with id" + film.getId() + "was not found.");
        }
        else
        {
           films.remove(film.getId());
           return  true;
        }

    }
}
