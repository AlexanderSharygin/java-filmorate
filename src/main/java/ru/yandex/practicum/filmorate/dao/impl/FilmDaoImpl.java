package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.models.Film;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDaoImpl(JdbcTemplate jdbcTemplate, UserDaoImpl userDao) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String SQL_GET_FILM = "SELECT f.id AS film_id, f.name, f.description , f.release_date, " +
            "f.duration, f.rate_id AS mpa_id, r.name AS mpa_name " +
            "FROM films f LEFT JOIN rates r on f.rate_id = r.id WHERE f.id = ?";
    private static final String SQL_GET_NEWEST_FILM = "SELECT TOP 1 F.ID AS film_id, F.NAME, F.DESCRIPTION ,F.RELEASE_DATE, " +
            "F.DURATION, F.RATE_ID AS MPA_ID, R.NAME AS MPA_NAME " +
            "from FILMS F LEFT JOIN RATES R on F.RATE_ID = R.ID ORDER BY F.ID DESC";
    private static final String SQL_GET_FILMS = "SELECT f.id AS film_id, f.name, f.description , f.release_date, " +
            "f.duration, f.rate_id AS mpa_id, r.name AS mpa_name " +
            "FROM films f LEFT JOIN rates r ON f.rate_id = r.id";
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
    public Optional<Film> findById(Long film_id) {
        List<Film> films = jdbcTemplate.query(SQL_GET_FILM, (rs, rowNum) -> makeFilm(rs), film_id);

        if (films.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(films.get(0));
    }

    @Override
    public Optional<Film> findNew() {
        List<Film> films = jdbcTemplate.query(SQL_GET_NEWEST_FILM, (rs, rowNum) -> makeFilm(rs));

        if (films.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(films.get(0));
    }

    @Override
    public List<Film> findAll() {

        return jdbcTemplate.query(SQL_GET_FILMS, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public void add(Film value) {
        jdbcTemplate.update(SQL_INSERT_FILM, value.getName(), value.getDescription(), value.getReleaseDate(),
                value.getDuration(), value.getMpa().getId());
    }

    @Override
    public void update(Film film) {
        jdbcTemplate.update(SQL_UPDATE_FILM, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
    }

    @Override
    public List<Film> findPopulars(Integer count) {
        return jdbcTemplate.query(SQL_GET_POPULAR_FILMS, (rs, rowNum) -> makeFilm(rs), count);
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
        film.setMpa(new Mpa(mpaId, mpaName));

        return film;
    }
}