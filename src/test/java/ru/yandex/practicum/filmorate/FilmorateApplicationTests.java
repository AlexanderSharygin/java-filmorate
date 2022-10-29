package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.models.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    private final UserDao userStorage;
    private final FilmDao filmStorage;

    private final FriendDao friendsStorage;

    private final GenreDao genreDao;

    private final MpaDao mpaDao;

    private final LikeDao likeStorage;

    @Test
    @Order(1)
    public void testGetUserById() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test");
        testUser.setEmail("Test@test.com");
        testUser.setLogin("Test");
        testUser.setBirthday(LocalDate.parse("1980-01-01"));
        userStorage.add(testUser);
        Optional<User> userOptional = userStorage.findById(1L);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    @Order(2)
    public void testGetUsers() {
        User testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setName("Test2");
        testUser2.setEmail("Test@test2.com");
        testUser2.setLogin("Test2");
        testUser2.setBirthday(LocalDate.parse("1980-01-01"));
        userStorage.add(testUser2);
        List<User> users = userStorage.findAll().get();
        assertThat(users.size()).isEqualTo(2);
        assertThat(users.get(0).getName()).isEqualTo("Test");
        assertThat(users.get(1).getName()).isEqualTo("Test2");
    }

    @Test
    @Order(3)
    public void addUser() {
        User testUser = new User();
        testUser.setId(3L);
        testUser.setName("Test3");
        testUser.setEmail("Test@test3.com");
        testUser.setLogin("Test3");
        testUser.setBirthday(LocalDate.parse("1980-01-01"));
        userStorage.add(testUser);
        Optional<User> user = userStorage.findById(3L);
        assertThat(user)
                .isPresent()
                .hasValueSatisfying(u ->
                        assertThat(u).hasFieldOrPropertyWithValue("Name", "Test3"));
    }

    @Test
    @Order(4)
    @AutoConfigureTestDatabase
    public void updateUser() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Upd");
        testUser.setEmail("Test@test.com");
        testUser.setLogin("Test");
        testUser.setBirthday(LocalDate.parse("1980-01-01"));
        userStorage.update(testUser);
        Optional<User> userOptional = userStorage.findById(1L);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                ).hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("Name", "Upd"));
    }

    @Test
    @Order(5)
    @AutoConfigureTestDatabase
    public void testAddFriend() {
        assertDoesNotThrow(() -> friendsStorage.add(1L, 2L));
    }

    @Test
    @Order(6)
    @AutoConfigureTestDatabase
    public void testConfirmFriend() {
        assertDoesNotThrow(() -> friendsStorage.confirm(1L, 2L));
    }

    @Test
    @Order(7)
    @AutoConfigureTestDatabase
    public void testRemoveFriend() {
        assertDoesNotThrow(() -> friendsStorage.remove(1L, 2L));
    }

    @Test
    @Order(8)
    @AutoConfigureTestDatabase
    public void testGetFriendsForUser() {
        friendsStorage.add(1L, 2L);
        friendsStorage.add(1L, 3L);
        List<User> users = userStorage.findFriends(1L).get();
        assertThat(users.size()).isEqualTo(2);
        assertThat(users.get(0).getName()).isEqualTo("Test2");
        assertThat(users.get(1).getName()).isEqualTo("Test3");
    }

    @Test
    @Order(9)
    @AutoConfigureTestDatabase
    public void testGetCommonFriendsForUsers() {
        friendsStorage.add(2L, 3L);
        List<User> users = userStorage.findCommonFriends(1L, 2L).get();
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getName()).isEqualTo("Test3");
    }

    @Test
    @Order(10)
    public void testGetFilmById() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Test");
        film.setDescription("Test");
        film.setReleaseDate(LocalDate.parse("1980-01-01"));
        film.setDuration(100);
        Mpa mpa = new Mpa(1L, "G");
        Genre genre = new Genre(1L, "Комедия");
        Genre genre2 = new Genre(2L, "Драма");
        film.setMpa(mpa);
        film.setGenres(List.of(genre, genre2));
        filmStorage.add(film);
        Optional<Film> filmOptional = filmStorage.findById(1L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    @Order(11)
    public void testGetFilms() {
        Film film = new Film();
        film.setId(2L);
        film.setName("Test2");
        film.setDescription("Test2");
        film.setReleaseDate(LocalDate.parse("1980-01-01"));
        film.setDuration(100);
        Mpa mpa = new Mpa(1L, "G");
        Genre genre = new Genre(1L, "Комедия");
        Genre genre2 = new Genre(2L, "Драма");
        film.setMpa(mpa);
        film.setGenres(List.of(genre, genre2));
        filmStorage.add(film);
        List<Film> films = filmStorage.findAll().get();
        assertThat(films.size()).isEqualTo(2);
        assertThat(films.get(0).getName()).isEqualTo("Test");
        assertThat(films.get(1).getName()).isEqualTo("Test2");
    }

    @Test
    @Order(12)
    public void addFilm() {
        Film film = new Film();
        film.setId(3L);
        film.setName("Test3");
        film.setDescription("Test3");
        film.setReleaseDate(LocalDate.parse("1980-01-01"));
        film.setDuration(100);
        Mpa mpa = new Mpa(1L, "G");
        Genre genre = new Genre(1L, "Комедия");
        Genre genre2 = new Genre(2L, "Драма");
        film.setMpa(mpa);
        film.setGenres(List.of(genre, genre2));
        filmStorage.add(film);
        Optional<Film> filmOptional = filmStorage.findById(3L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("Name", "Test3"));
    }

    @Test
    @Order(13)
    @AutoConfigureTestDatabase
    public void updateFilm() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Update");
        film.setDescription("Test");
        film.setReleaseDate(LocalDate.parse("1980-01-01"));
        film.setDuration(100);
        Mpa mpa = new Mpa(1L, "G");
        Genre genre = new Genre(1L, "Комедия");
        Genre genre2 = new Genre(2L, "Драма");
        film.setMpa(mpa);
        film.setGenres(List.of(genre, genre2));
        filmStorage.update(film);
        Optional<Film> filmOptional = filmStorage.findById(1L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("Name", "Update"));
    }

    @Test
    @Order(14)
    @AutoConfigureTestDatabase
    public void testGetGenreById() {
        Optional<Genre> genre = genreDao.findById(1L);
        assertThat(genre)
                .isPresent()
                .hasValueSatisfying(g ->
                        assertThat(g).hasFieldOrPropertyWithValue("id", 1L)
                ).hasValueSatisfying(g ->
                        assertThat(g).hasFieldOrPropertyWithValue("Name", "Комедия"));
    }

    @Test
    @Order(15)
    @AutoConfigureTestDatabase
    public void testGetGenres() {
        List<Genre> genres = genreDao.findAll().get();
        assertThat(genres.size()).isEqualTo(6);
        assertThat(genres.get(0).getName()).isEqualTo("Комедия");
        assertThat(genres.get(1).getName()).isEqualTo("Драма");
    }

    @Test
    @Order(16)
    @AutoConfigureTestDatabase
    public void testMPAById() {
        Optional<Mpa> mpa = mpaDao.findById(1L);
        assertThat(mpa)
                .isPresent()
                .hasValueSatisfying(m ->
                        assertThat(m).hasFieldOrPropertyWithValue("id", 1L)
                ).hasValueSatisfying(m ->
                        assertThat(m).hasFieldOrPropertyWithValue("Name", "G"));
    }

    @Test
    @Order(17)
    @AutoConfigureTestDatabase
    public void testAddLike() {
        likeStorage.add(1L, 1L);
        Optional<Like> like = likeStorage.find(1L, 1L);
        assertThat(like).isPresent();
    }

    @Test
    @Order(18)
    @AutoConfigureTestDatabase
    public void testRemoveLike() {
        likeStorage.remove(1L, 1L);
       assertTrue(likeStorage.find(1L, 1L).isEmpty());
    }

    @Test
    @Order(19)
    @AutoConfigureTestDatabase
    public void testGetPopularFilms() {
        likeStorage.add(1L, 1L);
        likeStorage.add(2L, 1L);
        likeStorage.add(2L, 2L);
        List<Film> films = filmStorage.findPopulars(2).get();
        assertThat(films.size()).isEqualTo(2);
        assertThat(films.get(0).getName()).isEqualTo("Test2");
        assertThat(films.get(1).getName()).isEqualTo("Update");
    }
}