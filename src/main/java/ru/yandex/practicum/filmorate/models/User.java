package ru.yandex.practicum.filmorate.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.utils.LocalDateDeserializer;
import ru.yandex.practicum.filmorate.utils.LocalDateSerializer;

import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;
    private String name;

    @NonNull
    @Pattern(regexp = "^\\S+$")
    private String login;

    @NonNull
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

    @NonNull
    @PastOrPresent
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthday;

    private Set<Long> friends;

    public User() {
        this.friends = new HashSet<>();
    }


    public User(@NonNull String login, @NonNull String email, @NonNull LocalDate birthday) {
        this.login = login;
        this.email = email;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }

    public Set<Long> getFriends() {
        return friends;
    }
}