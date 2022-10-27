package ru.yandex.practicum.filmorate.DAOs.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DAOs.MpaDao;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.exceptions.NotExistException;
import ru.yandex.practicum.filmorate.models.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_GET_MPA = "SELECT id, name FROM RATES WHERE id=? ";
    private static final String SQL_GET_MPAs = "SELECT id, name FROM RATES";

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MPA getMpaById(Long id) {
        try {
            return jdbcTemplate.queryForObject(SQL_GET_MPA, (rs, rowNum) -> makeMPA(rs), id);

        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException("MPA with id " + id + " not exists in the DB");
        }
    }

    @Override
    public List<MPA> getMpas() {
        try {
            return jdbcTemplate.query(SQL_GET_MPAs, (rs, rowNum) -> makeMPA(rs));
        } catch (RuntimeException e) {
            throw new BadRequestException("Something went wrong.");
        }
    }

    private MPA makeMPA(ResultSet rs) throws SQLException {
        return new MPA(rs.getLong("id"), rs.getString("NAME"));
    }
}
