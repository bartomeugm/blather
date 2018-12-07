package com.github.richardjwild.blather.persistence.mysql;

import com.github.richardjwild.blather.message.Message;
import com.github.richardjwild.blather.message.MessageRepository;
import com.github.richardjwild.blather.persistence.MessageDao;
import com.github.richardjwild.blather.user.User;

import java.util.stream.Stream;

public class MySqlMessageRepository implements MessageRepository {
    private MessageDao messageDao;

    public MySqlMessageRepository(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public void postMessage(User recipient, Message message) {
        messageDao.postMessage(message.toDto());
    }

    @Override
    public Stream<Message> allMessagesPostedTo(User recipient) {
        return messageDao.getMessagesFor(recipient.name()).stream().map(Message::from);
    }
}
