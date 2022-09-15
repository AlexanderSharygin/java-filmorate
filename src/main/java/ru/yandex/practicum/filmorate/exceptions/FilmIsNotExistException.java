package ru.yandex.practicum.filmorate.exceptions;

public class FilmIsNotExistException extends RuntimeException {
    public FilmIsNotExistException(String message) {
        super(message);
    }
}