package ru.yandex.practicum.filmorate.exceptions;

public class UserIsNotExistException extends RuntimeException {
    public UserIsNotExistException(String message) {
        super(message);
    }
}
