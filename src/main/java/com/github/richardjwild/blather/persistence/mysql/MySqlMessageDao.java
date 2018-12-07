package com.github.richardjwild.blather.persistence.mysql;


import com.github.richardjwild.blather.persistence.MessageDao;
import com.github.richardjwild.blather.persistence.MessageDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class MySqlMessageDao implements MessageDao {
    private DataSource dataSource;

    public MySqlMessageDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<MessageDto> getMessagesFor(String userName) {
        String sql = "SELECT recipient, text, post_time FROM messages WHERE recipient = ?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        RowMapper<MessageDto> messageDtoRowMapper = (rs, rowNum) -> new MessageDto(
                rs.getString("recipient"),
                rs.getString("text"),
                rs.getTimestamp("post_time"));

        return jdbcTemplate.query(sql, new Object[]{userName}, messageDtoRowMapper);
    }

    @Override
    public void postMessage(MessageDto messageDto) {
        String sql = "INSERT INTO messages (recipient, text, post_time) VALUES (?, ?, ?)";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.update(sql, messageDto.getRecipientName(), messageDto.getText(), messageDto.getTimestamp());
    }
}
