package com.example.chat.controller;

import com.example.chat.model.ChatMessage;
import com.example.chat.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.time.Instant;

@Controller
public class ChatController {

    @Autowired
    private ChatMessageRepository messageRepository;

    @MessageMapping("/chat/{ticketId}")
    @SendTo("/topic/chat/{ticketId}")
    public ChatMessage handleMessage(@DestinationVariable String ticketId, ChatMessage message) {
        message.setTicketId(ticketId);
        message.setTimestamp(Instant.now());
        messageRepository.save(message);
        return message;
    }
}