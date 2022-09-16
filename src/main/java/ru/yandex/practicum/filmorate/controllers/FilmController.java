package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.models.Film;

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
    public ResponseEntity<?> create(@RequestBody Film film) {
        validate(film);
        if (films.stream()
                .anyMatch(k -> k.getName().equals(film.getName())
                        && k.getReleaseDate().equals(film.getReleaseDate()))) {
            log.error("Фильм с названием {} и датой выпуска {} является дубликатом уже существующего фильма", film.getName(), film.getReleaseDate());
            throw new AlreadyExistException("Film is already exist in the DB.");
        }
        film.setId(idCounter);
        films.add(film);
        idCounter++;
        log.info("Добавлен фильм {} с датой выпуска {}", film.getName(), film.getReleaseDate());
        return ResponseEntity.ok(film);
    }

    @PutMapping(value = "/films")
    public ResponseEntity<?> update(@RequestBody Film film) {
        validate(film);
        Optional<Film> existedFilm = films.stream()
                .filter(k -> k.getId().equals(film.getId()))
                .findFirst();
        if (existedFilm.isEmpty()) {
            log.error("Фильм с id {} не существует в базе", film.getId());
            throw new NotExistException("Film with specified name/releaseDate was not find.");
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
        if (film.getName().isBlank()) {
            log.warn("В запросе передано пустое имя фильма");
            throw new ValidationException("Film name can't be empty");
        }
        if (film.getDescription().length() > 200) {
            log.warn("В запросе передано невалидное описание фильма -  {}", film.getDescription());
            throw new ValidationException("Max. length for the Description value is limited by 200 chars");
        }
        if (film.getReleaseDate().isBefore(MIN_DATE)) {
            log.warn("В запросе передана невалидная дата релиза фильма -  {}", film.getReleaseDate());
            throw new ValidationException("Release date can be less than 28/12/1895");
        }
        if (film.getDuration() < 0 || film.getDuration() == 0) {
            log.warn("В запросе передана невалидная продолжительность фильма -  {}", film.getDuration());
            throw new ValidationException("Film Duration should be more than zero");
        }
    }
}