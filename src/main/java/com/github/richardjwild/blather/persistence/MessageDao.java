package com.github.richardjwild.blather.persistence;

import java.util.List;

public interface MessageDao {
    List<MessageDto> getMessagesFor(String userName);

    void postMessage(MessageDto messageDto);
}
