package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.utils.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.utils.LocalDateSerializer;

import java.time.LocalDate;

@Data
public class User {
    private Integer id;
    private String name;

    @NonNull
    private String login;

    @NonNull
    private String email;

    @NonNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthday;

    public User() {
    }

    public User(@NonNull String login, @NonNull String email, @NonNull LocalDate birthday) {
        this.login = login;
        this.email = email;
        this.birthday = birthday;
    }
}
