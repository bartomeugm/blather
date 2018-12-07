package com.github.richardjwild.blather.persistence.mongodb;

import com.github.richardjwild.blather.message.Message;
import com.github.richardjwild.blather.message.MessageRepository;
import com.github.richardjwild.blather.persistence.MessageDao;
import com.github.richardjwild.blather.persistence.MessageDto;
import com.github.richardjwild.blather.user.User;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.gradle.internal.impldep.org.testng.AssertJUnit.assertEquals;
import static org.mockito.Mockito.*;

public class MongoDbMessageRepositoryShould {

    private MessageDao messageDao;
    private MessageRepository repository;
    private final User user = new User("user1");

    @Before
    public void setUp() {
        messageDao = mock(MongoDbMessageDao.class);
        repository = new MongoDbMessageRepository(messageDao);
    }

    @Test
    public void return_empty_collection_when_no_messages_posted_to_recipient() {
        when(messageDao.getMessagesFor(user.name())).thenReturn(Collections.emptyList());

        Stream<Message> actualMessages = repository.allMessagesPostedTo(user);

        assertEquals(0, actualMessages.count());
    }

    @Test
    public void ask_dao_to_retrieve_messages() {
        MessageDto expectedMessageDto = new MessageDto(user.name(), "Hello world", Timestamp.from(Instant.EPOCH));
        Message expectedMessage = Message.from(expectedMessageDto);
        when(messageDao.getMessagesFor(user.name())).thenReturn(asList(expectedMessageDto));

        Stream<Message> messages = repository.allMessagesPostedTo(user);

        verify(messageDao).getMessagesFor(user.name());
        assertEquals(messages.findFirst().get(), expectedMessage);
    }

    @Test
    public void tell_dao_to_post_messages() {
        Message message = new Message(user, "Hello world", Instant.EPOCH);
        MessageDto messageDto = message.toDto();

        repository.postMessage(user, message);

        verify(messageDao).postMessage(messageDto);
    }

}
