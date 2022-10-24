package ru.yandex.practicum.filmorate.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Friend {
    @NonNull
    Long userId;

    @NonNull
    Long friendId;

    @NonNull
    Integer statusId;

    public Friend() {
    }
}