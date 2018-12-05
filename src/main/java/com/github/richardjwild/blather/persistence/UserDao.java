package com.github.richardjwild.blather.persistence;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;


public class UserDao {
    private DataSource dataSource;
    private Connection connection;

    public UserDao(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    public void saveUser(String name) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("INSERT INTO users values(?)", name);
    }

    public String findUser(String name) {
       try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            return jdbcTemplate.queryForObject("SELECT name FROM users WHERE name = ?", String.class, name);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException){
            return null;
        }
    }
}
