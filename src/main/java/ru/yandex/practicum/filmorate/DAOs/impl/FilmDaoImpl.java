package ru.yandex.practicum.filmorate.DAOs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAOs.FilmDao;
import ru.yandex.practicum.filmorate.exceptions.AlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class FilmDaoImpl implements FilmDao {
    @Value("${filmorate.minDate}")
    private String minDate;
    private final JdbcTemplate jdbcTemplate;

    private final UserDaoImpl userDao;


    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate, UserDaoImpl userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    private static final String SQL_GET_FILM = "SELECT f.id AS film_id, f.name, f.description , f.release_date, " +
            "f.duration, f.rate_id AS mpa_id, r.name AS mpa_name " +
            "FROM films f LEFT JOIN rates r on f.rate_id = r.id where f.id = ?";
    private static final String SQL_GET_NEWEST_FILM = "SELECT TOP 1 F.ID AS film_id, F.NAME, F.DESCRIPTION ,F.RELEASE_DATE, " +
            "F.DURATION, F.RATE_ID AS MPA_ID, R.NAME AS MPA_NAME " +
            "from FILMS F LEFT JOIN RATES R on F.RATE_ID = R.ID ORDER BY F.ID DESC";
    private static final String SQL_GET_GENRES_FOR_FILM = "SELECT g.genre_id, g.name " +
            "FROM genres g JOIN genres_films gf ON g.genre_id=gf.genre_id WHERE gf.films_id = ?";
    private static final String SQL_GET_FILMS = "SELECT f.id AS film_id, f.name, f.description , f.release_date, " +
            "f.duration, f.rate_id AS mpa_id, r.name AS mpa_name " +
            "FROM films f LEFT JOIN rates r on f.rate_id = r.id";
    private static final String SQL_INSERT_FILM = "INSERT INTO films (name, description, release_date, duration, rate_id) " +
            "VALUES(?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE_FILM = "UPDATE FILMS " +
            "SET name=?, description=?, release_date=?, duration=?, rate_id=? WHERE id=?";

    private static final String SQL_GET_POPULAR_FILMS = "SELECT f.id AS film_id, f.name, f.description, f.release_date, " +
            "f.duration, f.rate_id AS mpa_id, r.name AS mpa_name " +
            "FROM films f LEFT JOIN rates r ON f.rate_id = r.id " +
            "LEFT JOIN (SELECT film_id, COUNT(film_id) AS likes_count FROM likes GROUP BY film_id) l " +
            " ON f.id = l.film_id ORDER BY likes_count DESC LIMIT ?";

    @Override
    public Optional<Film> getFilmById(Long film_id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_FILM, (rs, rowNum) -> makeFilm(rs), film_id));
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("Film with id " + film_id + " not exists in the DB");
        }
    }

    @Override
    public Optional<Film> getNewestFilm() {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_NEWEST_FILM, (rs, rowNum) -> makeFilm(rs)));
        } catch (RuntimeException e) {
            throw new BadRequestException("Something went wrong.");
        }
    }

    @Override
    public List<Film> getFilms() {
        try {
            return jdbcTemplate.query(SQL_GET_FILMS, (rs, rowNum) -> makeFilm(rs));
        } catch (RuntimeException e) {
            throw new BadRequestException("Something went wrong.");
        }
    }

    @Override
    public void addFilm(Film film) {
        try {
            jdbcTemplate.update(SQL_INSERT_FILM, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        } catch (DuplicateKeyException e) {
            throw new AlreadyExistException("Film already exists in the DB");
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("Mpa rate with specified id not exists in the DB");
        }
    }

    @Override
    public void updateFilm(Film film) {
        try {
            jdbcTemplate.update(SQL_UPDATE_FILM, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("Film with id " + film.getId() + " not exists in the DB");
        }
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        try {
            return jdbcTemplate.query(SQL_GET_POPULAR_FILMS, (rs, rowNum) -> makeFilm(rs), count);
        } catch (RuntimeException e) {
            throw new BadRequestException("Something went wrong.");
        }
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        Long id = rs.getLong("film_id");
        String name = rs.getString("NAME");
        String description = rs.getString("DESCRIPTION");
        LocalDate releaseDate = rs.getDate("RELEASE_DATE").toLocalDate();
        Integer duration = rs.getInt("DURATION");
        Long mpaId = rs.getLong("MPA_ID");
        String mpaName = rs.getString("MPA_NAME");
        film.setId(id);
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(releaseDate);
        film.setDuration(duration);
        film.setMpa(new MPA(mpaId, mpaName));

        return film;
    }
}