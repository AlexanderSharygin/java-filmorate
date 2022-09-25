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



    @GetMapping("/films")
    public ResponseEntity<?> getAll() {
        log.info("Текущее количество фильмов: {}", films.size());
        return ResponseEntity.ok(films);
    }

    @PostMapping(value = "/films")
    public ResponseEntity<?> create(@Valid @RequestBody Film film) {
        validate(film);

    }

    @PutMapping(value = "/films")
    public ResponseEntity<?> update(@Valid @RequestBody Film film) {
        validate(film);

    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE)) {
            log.warn("В запросе передана невалидная дата релиза фильма -  {}", film.getReleaseDate());
            throw new ValidationException("Release date can be less than 28/12/1895");
        }

    }
}