package com.github.richardjwild.blather.persistence;

import com.github.richardjwild.blather.Blather;
import com.github.richardjwild.blather.user.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import javax.swing.*;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FollowersDao {
    private Connection connection;
    private DataSource dataSource;

    public FollowersDao(Connection connection) {

        this.connection = connection;
    }

    public FollowersDao(DataSource dataSource) {

        this.dataSource = dataSource;
    }

    public void saveFollowees(String follower, Set<User> followees) {
        try {
            connection.setAutoCommit(false);
            deleteFollowees(follower);

            followees.forEach(followee -> {
                try {
                    addFollowees(follower, followee.name());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            connection.commit();
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteFollowees(String follower) throws SQLException {
        PreparedStatement statement;
        statement = connection.prepareStatement("DELETE FROM followers WHERE follower = ?");
        statement.setString(1, follower);
        statement.execute();
    }

    private void addFollowees(String follower, String followee) throws SQLException {
        PreparedStatement statement;
        statement = connection.prepareStatement("INSERT INTO followers VALUES (?, ?)");
        statement.setString(1, follower);
        statement.setString(2, followee);
        statement.execute();
    }

    public Set<String> getFollowees(String follower) {
        String sql = "SELECT followee FROM followers WHERE follower = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        RowMapper<String> followeeRowMapper = (rs, rowNum)->
                rs.getString("followee");

        List<String> followees = jdbcTemplate.query(sql, new Object[]{follower}, followeeRowMapper);
        return new HashSet<>(followees);
    }

    public static void main(String[] args) {
        DataSource dataSource = Blather.newMySQLDataSource();
        FollowersDao followersDao = new FollowersDao(dataSource);

        Set<String> followees = followersDao.getFollowees("testuser");

        followees.forEach(System.out::println);
    }
}
