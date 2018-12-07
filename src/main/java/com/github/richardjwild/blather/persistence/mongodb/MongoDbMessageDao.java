package com.github.richardjwild.blather.persistence.mongodb;

import com.github.richardjwild.blather.persistence.MessageDao;
import com.github.richardjwild.blather.persistence.MessageDto;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MongoDbMessageDao implements MessageDao {
    private MongoCollection<Document> messages;

    public MongoDbMessageDao(MongoCollection<Document> messages) {
        this.messages = messages;
    }

    @Override
    public List<MessageDto> getMessagesFor(String userName) {
        FindIterable<Document> messagesList = messages.find(eq("recipient_name", userName));
        List<MessageDto> messageDtos =  new ArrayList<>();

        for (Document document: messagesList) {
            String recipientName = document.getString("recipient_name");
            String messageText = document.getString("text");
            Timestamp timestamp = Timestamp.from(document.getDate("timestamp").toInstant());
            messageDtos.add(new MessageDto(recipientName, messageText, timestamp));
        }

        return messageDtos;
    }

    @Override
    public void postMessage(MessageDto messageDto) {
        Document document = new Document()
                .append("recipient_name", messageDto.getRecipientName())
                .append("text", messageDto.getText())
                .append("timestamp", messageDto.getTimestamp());
        messages.insertOne(document);
    }
}
