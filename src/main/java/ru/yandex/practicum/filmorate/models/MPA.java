package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class MPA {
    @NonNull
    Long id;

    @NonNull
    String name;

    public MPA() {
    }

}