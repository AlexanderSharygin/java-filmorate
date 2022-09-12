package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exceptions.FilmIsNotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class FilmController {

    private final List<Film> films = new ArrayList<>();
    private int idCounter = 0;
    private final LocalDate minDate = LocalDate.of(1895, 12, 28);

    @GetMapping("/films")
    public List<Film> getAll() {
        return films;
    }


    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
       validate(film);
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
       validate(film);
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

    private void validate(Film film)
    {
        if (film.getName().isBlank()) {
            throw new ValidationException("Film name can't be empty");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Max. length for the Description value is limited by 200 chars");
        }
        if (film.getReleaseDate().isBefore(minDate)) {
            throw new ValidationException("Release date can be less than 28/12/1895");
        }
        if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            throw new ValidationException("Film Duration should be more than zero");
        }
    }


}