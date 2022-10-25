package ru.yandex.practicum.filmorate.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.utils.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.utils.LocalDateSerializer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Film {
    private Long id;

    @NonNull
    @NotBlank
    @Length(max = 50)
    private String name;

    @NonNull
    @Length(max = 200)
    private String description;

    @NonNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Past()
    private LocalDate releaseDate;

    @Min(1)
    private Integer duration;

    @NonNull
    private List<Genre> genres;

    MPA mpa;

    public Film() {
        this.genres = new ArrayList<>();
        mpa = new MPA();
    }
}