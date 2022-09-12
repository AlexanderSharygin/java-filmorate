package ru.yandex.practicum.filmorate;

import lombok.Data;
import lombok.NonNull;

@Data
public class ErrorMessage {
    @NonNull
    private String message;
}
