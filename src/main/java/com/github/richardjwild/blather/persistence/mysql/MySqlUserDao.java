package com.github.richardjwild.blather.persistence.mysql;

import com.github.richardjwild.blather.persistence.UserDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;


public class MySqlUserDao implements UserDao {
    private DataSource dataSource;

    public MySqlUserDao(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    @Override
    public void saveUser(String name) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("INSERT INTO users values(?)", name);
    }

    @Override
    public String findUser(String name) {
       try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            return jdbcTemplate.queryForObject("SELECT name FROM users WHERE name = ?", String.class, name);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException){
            return null;
        }
    }
}
