package com.github.richardjwild.blather.persistence;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@EnableAutoConfiguration
public class UserDao {
    private DataSource dataSource;
    private Connection connection;

    public UserDao(Connection connection) {
        this.connection = connection;
    }

    public UserDao(DriverManagerDataSource dataSource) {

        this.dataSource = dataSource;
    }

    public void saveUser(String name) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update("INSERT INTO users values(?)", name);
    }

    public String findUser(String name) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.queryForObject("SELECT name FROM users WHERE name = ?", String.class, name);
    }


    public static void main(String[] args) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/blather?user=root&password=password");
        dataSource.setUsername("root");
        dataSource.setPassword("password");

        // Inject the datasource into the dao
        UserDao dao = new UserDao(dataSource);
        String username = dao.findUser("testuser");

        String insertedUser = "inserted_user";
        dao.saveUser(insertedUser);
        System.out.println("Username: " + dao.findUser(insertedUser));
    }
}
