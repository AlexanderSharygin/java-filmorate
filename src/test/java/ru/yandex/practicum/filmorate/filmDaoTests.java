package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.DAOs.FilmDao;
import ru.yandex.practicum.filmorate.DAOs.UserDao;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Genre;
import ru.yandex.practicum.filmorate.models.MPA;
import ru.yandex.practicum.filmorate.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class filmDaoTests {
    private final FilmDao filmStorage;
    private final UserDao userStorage;

    @Test
    @Order(1)
    public void testGetFilmById() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Test");
        film.setDescription("Test");
        film.setReleaseDate(LocalDate.parse("1980-01-01"));
        film.setDuration(100);
        MPA mpa = new MPA(1L, "G");
        Genre genre = new Genre(1L, "Комедия");
        Genre genre2 = new Genre(2L, "Драма");
        film.setMpa(mpa);
        film.setGenres(List.of(genre, genre2));
        filmStorage.addFilm(film);
        Optional<Film> filmOptional = filmStorage.getFilmById(1L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    @Order(2)
    public void testGetFilms() {
        Film film = new Film();
        film.setId(2L);
        film.setName("Test2");
        film.setDescription("Test2");
        film.setReleaseDate(LocalDate.parse("1980-01-01"));
        film.setDuration(100);
        MPA mpa = new MPA(1L, "G");
        Genre genre = new Genre(1L, "Комедия");
        Genre genre2 = new Genre(2L, "Драма");
        film.setMpa(mpa);
        film.setGenres(List.of(genre, genre2));
        filmStorage.addFilm(film);
        List<Film> films = filmStorage.getFilms();
        assertThat(films.size()).isEqualTo(2);
        assertThat(films.get(0).getName()).isEqualTo("Test");
        assertThat(films.get(1).getName()).isEqualTo("Test2");
    }

    @Test
    @Order(3)
    public void addFilm() {
        Film film = new Film();
        film.setId(3L);
        film.setName("Test3");
        film.setDescription("Test3");
        film.setReleaseDate(LocalDate.parse("1980-01-01"));
        film.setDuration(100);
        MPA mpa = new MPA(1L, "G");
        Genre genre = new Genre(1L, "Комедия");
        Genre genre2 = new Genre(2L, "Драма");
        film.setMpa(mpa);
        film.setGenres(List.of(genre, genre2));
        Optional<Film> filmOptional = filmStorage.addFilm(film);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("id", 3L));
    }

    @Test
    @Order(4)
    @AutoConfigureTestDatabase
    public void updateUser() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Update");
        film.setDescription("Test");
        film.setReleaseDate(LocalDate.parse("1980-01-01"));
        film.setDuration(100);
        MPA mpa = new MPA(1L, "G");
        Genre genre = new Genre(1L, "Комедия");
        Genre genre2 = new Genre(2L, "Драма");
        film.setMpa(mpa);
        film.setGenres(List.of(genre, genre2));
        Optional<Film> filmOptional = filmStorage.updateFilm(film);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("id", 1L)
                ).hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("Name", "Update"));
    }


    @Test
    @Order(5)
    @AutoConfigureTestDatabase
    public void testGetGenreById() {
        Optional<Genre> genre = filmStorage.getGenreById(1L);
        assertThat(genre)
                .isPresent()
                .hasValueSatisfying(g ->
                        assertThat(g).hasFieldOrPropertyWithValue("id", 1L)
                ).hasValueSatisfying(g ->
                        assertThat(g).hasFieldOrPropertyWithValue("Name", "Комедия"));
    }

    @Test
    @Order(6)
    @AutoConfigureTestDatabase
    public void testGetGenres() {
        List<Genre> genres = filmStorage.getGenres();
        assertThat(genres.size()).isEqualTo(6);
        assertThat(genres.get(0).getName()).isEqualTo("Комедия");
        assertThat(genres.get(1).getName()).isEqualTo("Драма");
    }

    @Test
    @Order(7)
    @AutoConfigureTestDatabase
    public void testMPAById() {
        Optional<MPA> mpa = filmStorage.getMPAById(1L);
        assertThat(mpa)
                .isPresent()
                .hasValueSatisfying(m ->
                        assertThat(m).hasFieldOrPropertyWithValue("id", 1L)
                ).hasValueSatisfying(m ->
                        assertThat(m).hasFieldOrPropertyWithValue("Name", "G"));
    }

    @Test
    @Order(8)
    @AutoConfigureTestDatabase
    public void testAddLike() {
        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test");
        testUser.setEmail("Test@test.com");
        testUser.setLogin("Test");
        testUser.setBirthday(LocalDate.parse("1980-01-01"));
        userStorage.addUser(testUser);
        assertTrue(filmStorage.addLIke(1L, 1L));
    }

    @Test
    @Order(9)
    @AutoConfigureTestDatabase
    public void testRemoveLike() {
        assertTrue(filmStorage.removeLike(1L, 1L));
    }

    @Test
    @Order(10)
    @AutoConfigureTestDatabase
    public void testGetPopularFilms() {
        User testUser = new User();
        testUser.setId(2L);
        testUser.setName("Test2");
        testUser.setEmail("Test@test2.com");
        testUser.setLogin("Test2");
        testUser.setBirthday(LocalDate.parse("1980-01-01"));
        userStorage.addUser(testUser);
        assertTrue(filmStorage.addLIke(1L, 1L));
        assertTrue(filmStorage.addLIke(2L, 1L));
        assertTrue(filmStorage.addLIke(2L, 2L));
        List<Film> films = filmStorage.getPopularFilms(2);
        assertThat(films.size()).isEqualTo(2);
        assertThat(films.get(0).getName()).isEqualTo("Test2");
        assertThat(films.get(1).getName()).isEqualTo("Update");
    }
}
