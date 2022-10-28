package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.models.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;
    private static final String SQL_GET_MPA = "SELECT id, name FROM RATES WHERE id=? ";
    private static final String SQL_GET_MPAs = "SELECT id, name FROM RATES";

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> findById(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_MPA, (rs, rowNum) -> makeMPA(rs), id));
    }

    @Override
    public Optional<List<Mpa>> findAll() {
        return Optional.of(jdbcTemplate.query(SQL_GET_MPAs, (rs, rowNum) -> makeMPA(rs)));
    }

    private Mpa makeMPA(ResultSet rs) throws SQLException {
        return new Mpa(rs.getLong("id"), rs.getString("NAME"));
    }
}