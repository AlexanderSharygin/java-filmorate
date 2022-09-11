package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.FilmIsNotExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class FilmController {


    private final List<Film> films = new ArrayList<>();
    private int idCounter = 0;

    @GetMapping("/films")
    public List<Film> getAll() {
        return films;
    }


    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        if (films.stream()
                .anyMatch(k -> k.getName().equals(film.getName())
                        && k.getReleaseDate().equals(film.getReleaseDate()))) {
            throw new FilmAlreadyExistsException("Film is already exist in the DB.");
        }
        film.setId(idCounter);
        films.add(film);
        idCounter++;
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) {
        Optional<Film> existedFilm = films.stream()
                .filter(k -> k.getName().equals(film.getName())
                        && k.getReleaseDate().equals(film.getReleaseDate()))
                .findFirst();
        if (existedFilm.isEmpty()) {
            throw new FilmIsNotExistException("User with specified email is not find.");
        } else {
            existedFilm.get().setReleaseDate(film.getReleaseDate());
            existedFilm.get().setDescription(film.getDescription());
            existedFilm.get().setName(film.getName());
            existedFilm.get().setDuration(film.getDuration());
            return existedFilm.get();
        }
    }
}