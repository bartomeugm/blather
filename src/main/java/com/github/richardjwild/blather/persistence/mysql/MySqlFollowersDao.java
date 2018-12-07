package com.github.richardjwild.blather.persistence.mysql;

import com.github.richardjwild.blather.persistence.FollowersDao;
import com.github.richardjwild.blather.user.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MySqlFollowersDao implements FollowersDao {
    private Connection connection;
    private DataSource dataSource;
    private DataSourceTransactionManager transactionManager;

    public MySqlFollowersDao(DataSource dataSource, DataSourceTransactionManager transactionManager) {

        this.dataSource = dataSource;
        this.transactionManager = transactionManager;
    }

    @Override
    @Transactional("tjtJTransactionManager")
    public void saveFollowees(String follower, Set<User> followees) {
        TransactionStatus status = null;
        try {
            TransactionDefinition def = new DefaultTransactionDefinition();
            status = transactionManager.getTransaction(def);
            deleteFollowees(follower);

            TransactionStatus finalStatus = status;
            followees.forEach(followee -> {
                try {
                    addFollowees(follower, followee.name());
                } catch (DataAccessException e) {
                    transactionManager.rollback(finalStatus);
                    e.printStackTrace();
                }
            });
            transactionManager.commit(status);
        } catch (DataAccessException e) {
            transactionManager.rollback(status);
            System.err.println("Connection failed: " + e.getMessage());
        }
    }

    @Override
    public Set<String> getFollowees(String follower) {
        String sql = "SELECT followee FROM followers WHERE follower = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        RowMapper<String> followeeRowMapper = (rs, rowNum) ->
                rs.getString("followee");

        List<String> followees = jdbcTemplate.query(sql, new Object[]{follower}, followeeRowMapper);
        return new HashSet<>(followees);
    }

    private void deleteFollowees(String follower) {
        String sql = "DELETE FROM followers WHERE follower = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(sql, follower);
    }

    private void addFollowees(String follower, String followee) {
        String sql = "INSERT INTO followers VALUES (?, ?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(sql, new Object[]{follower, followee});
    }
}
