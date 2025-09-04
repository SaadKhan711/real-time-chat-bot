package com.example.chat.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    // This map stores all active chat rooms.
    // The key is the ticket ID, and the value is a list of all people in that room.
    private static final Map<String, CopyOnWriteArrayList<WebSocketSession>> chatRooms = new ConcurrentHashMap<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // This is just a confirmation that someone connected.
        System.out.println("New WebSocket connection established: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JsonNode jsonNode = objectMapper.readTree(payload);
        String type = jsonNode.get("type").asText();

        if ("subscribe".equalsIgnoreCase(type)) {
            handleSubscription(session, jsonNode);
        } else if ("chat".equalsIgnoreCase(type)) {
            handleChatMessage(jsonNode);
        }
    }

    private void handleSubscription(WebSocketSession session, JsonNode jsonNode) {
        String ticketId = jsonNode.get("ticketId").asText();
        // Create the chat room if it doesn't exist, then add the new person.
        chatRooms.computeIfAbsent(ticketId, k -> new CopyOnWriteArrayList<>()).add(session);
        System.out.println("Session " + session.getId() + " subscribed to ticket " + ticketId);
    }

    private void handleChatMessage(JsonNode jsonNode) throws IOException {
        String ticketId = jsonNode.get("ticketId").asText();
        CopyOnWriteArrayList<WebSocketSession> sessions = chatRooms.get(ticketId);

        if (sessions != null && !sessions.isEmpty()) {
            System.out.println("Broadcasting message to " + sessions.size() + " subscribers for ticket " + ticketId);
            // Go through every person in the chat room and send them the message.
            for (WebSocketSession subscriber : sessions) {
                if (subscriber.isOpen()) {
                    subscriber.sendMessage(new TextMessage(objectMapper.writeValueAsString(jsonNode)));
                }
            }
        }
    }
}
