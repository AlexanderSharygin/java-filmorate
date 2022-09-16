package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class FilmController {

    private static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
    private final List<Film> films = new ArrayList<>();
    private long idCounter = 1;

    @GetMapping("/films")
    public ResponseEntity<?> getAll() {
        log.info("Текущее количество фильмов: {}", films.size());
        return ResponseEntity.ok(films);
    }

    @PostMapping(value = "/films")
    public ResponseEntity<?> create(@Valid @RequestBody Film film) {
        validate(film);
        if (films.stream()
                .anyMatch(k -> k.getName().equals(film.getName())
                        && k.getReleaseDate().equals(film.getReleaseDate()))) {
            throw new AlreadyExistException("Film with name " + film.getName() + " and release date " + film.getReleaseDate() + " already exists in the DB.");
        }
        film.setId(idCounter);
        films.add(film);
        idCounter++;
        log.info("Добавлен фильм {} с датой выпуска {}", film.getName(), film.getReleaseDate());
        return ResponseEntity.ok(film);
    }

    @PutMapping(value = "/films")
    public ResponseEntity<?> update(@Valid @RequestBody Film film) {
        validate(film);
        Optional<Film> existedFilm = films.stream()
                .filter(k -> k.getId().equals(film.getId()))
                .findFirst();
        if (existedFilm.isEmpty()) {
            throw new NotExistException("Film with id" + film.getId() + "was not find.");
        } else {
            existedFilm.get().setReleaseDate(film.getReleaseDate());
            existedFilm.get().setDescription(film.getDescription());
            existedFilm.get().setName(film.getName());
            existedFilm.get().setDuration(film.getDuration());
            log.info("Фильм {} с датой выпуска {} обновлен", film.getName(), film.getReleaseDate());
            return ResponseEntity.ok(existedFilm.get());
        }
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE)) {
            log.warn("В запросе передана невалидная дата релиза фильма -  {}", film.getReleaseDate());
            throw new ValidationException("Release date can be less than 28/12/1895");
        }

    }
}