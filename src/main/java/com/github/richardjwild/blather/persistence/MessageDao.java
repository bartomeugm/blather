package com.github.richardjwild.blather.persistence;


import com.github.richardjwild.blather.Blather;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import org.springframework.jdbc.core.ResultSetExtractor;

public class MessageDao {
    private Connection connection;
    private DataSource dataSource;

    public MessageDao(Connection connection) {

        this.connection = connection;
    }

    public MessageDao(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public List<MessageDto> getMessagesFor(String userName) {
            String sql = "SELECT recipient, text, post_time FROM messages WHERE recipient = ?";
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        RowMapper<MessageDto> messageDtoRowMapper = (rs, rowNum) -> new MessageDto(
                    rs.getString("recipient"),
                    rs.getString("text"),
                    rs.getTimestamp("post_time"));

        return jdbcTemplate.query(sql, new Object[]{userName}, messageDtoRowMapper);
    }

    public void postMessage(MessageDto messageDto) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO messages (recipient, text, post_time) VALUES (?, ?, ?)");
            statement.setString(1, messageDto.getRecipientName());
            statement.setString(2, messageDto.getText());
            statement.setTimestamp(3, messageDto.getTimestamp());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DataSource dataSource = Blather.newMySQLDataSource();
        MessageDao messageDao = new MessageDao(dataSource);

        List<MessageDto> testuser = messageDao.getMessagesFor("testuser");

        testuser.forEach(System.out::println);

    }
}
