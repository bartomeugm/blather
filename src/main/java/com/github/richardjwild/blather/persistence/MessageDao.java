package com.github.richardjwild.blather.persistence;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

public class MessageDao {
    private DataSource dataSource;

    public MessageDao(DataSource dataSource) {
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
        String sql = "INSERT INTO messages (recipient, text, post_time) VALUES (?, ?, ?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(sql, messageDto.getRecipientName(), messageDto.getText(), messageDto.getTimestamp());
    }
}
