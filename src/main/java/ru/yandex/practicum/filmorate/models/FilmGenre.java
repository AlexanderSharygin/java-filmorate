package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class FilmGenre {

    @NonNull
    private Integer filmId;
    @NonNull
    private Integer userId;

    public FilmGenre() {
    }
}
