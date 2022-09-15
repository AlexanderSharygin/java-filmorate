package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.utils.DurationSerializer;
import ru.yandex.practicum.filmorate.utils.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.utils.LocalDateSerializer;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private Integer id;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate releaseDate;

    @NonNull
    @JsonSerialize(using = DurationSerializer.class)
    private Duration duration;

    public Film() {
    }

    public Film(@NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, @NonNull Duration duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
