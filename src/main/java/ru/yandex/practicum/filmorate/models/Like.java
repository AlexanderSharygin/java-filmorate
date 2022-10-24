package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;


@Data
@AllArgsConstructor
public class Like {
    @NonNull
    Long userId;

    @NonNull
    Long filmId;

    public Like() {
    }
}