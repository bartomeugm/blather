package com.github.richardjwild.blather;

import com.github.richardjwild.blather.application.Application;
import com.github.richardjwild.blather.application.ApplicationBuilder;
import com.github.richardjwild.blather.io.ConsoleInput;
import com.github.richardjwild.blather.io.ConsoleOutput;
import com.github.richardjwild.blather.persistence.*;
import com.github.richardjwild.blather.time.SystemClock;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Blather {

    public static void main(String[] args) {
        Connection connection = newMySQLConnection();
        DataSource dataSource = newMySQLDataSource();
        Application application = ApplicationBuilder.build(new ConsoleInput(),
                new ConsoleOutput(),
                new SystemClock(),
                new MySqlUserRepository(new UserDao(dataSource), new FollowersDao(connection)), new MySqlMessageRepository(new MessageDao(connection)));
        application.run();
        closeConnection(connection);
    }

    private static void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not close SQL connection: " + e);
        }
    }

    private static Connection newMySQLConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/blather" +
                    "?user=root&password=password");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot begin SQL: " + e.getMessage());
        }
    }

    private static DataSource newMySQLDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/blather?user=root&password=password");
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        return dataSource;
    }
}
