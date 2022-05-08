package com.uppa.project421.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public static Map<Integer, String> onlinePlayers = new HashMap<Integer, String>();


    @EventListener
        public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
        StompHeaderAccessor stompAccessor = StompHeaderAccessor.wrap(event.getMessage());
        GenericMessage connectHeader = (GenericMessage)stompAccessor.getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);    // FIXME find a way to pass the username to the server
        Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) connectHeader.getHeaders().get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
        AtomicReference<Boolean> addUserOnline = new AtomicReference<>(false);
        AtomicInteger id = new AtomicInteger();
        AtomicReference<String> username = new AtomicReference<>("");
        nativeHeaders.forEach((index, header) -> {
            if (index.toString().equals("userId")) {
                id.set(Integer.valueOf(removeFirstandLast(header.toString())));
            }
            else if (index.toString().equals("username")) {
                username.set(removeFirstandLast(header.toString()));
            }
            if (index.toString().equals("username") || index.toString().equals("userId")) {
                addUserOnline.set(true);
                username.set(removeFirstandLast(header.toString()));
            }
        });
        if (addUserOnline.get() == true) {
            onlinePlayers.put(id.get(),username.get());
        }
        for (String value : onlinePlayers.values()) {
            System.out.println(value);
        }
        for (int value : onlinePlayers.keySet()) {
            System.out.println(value);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        int id = (int) headerAccessor.getSessionAttributes().get("userId");

        if(username != null) {
            logger.info("User Disconnected : " + username);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType("LEAVE");
            chatMessage.setUsername(username);
            chatMessage.setUserId(id);

            messagingTemplate.convertAndSend("/topic/publicChatRoom", chatMessage);
        }
    }

    public static String
    removeFirstandLast(String str)
    {

        // Removing first and last character
        // of a string using substring() method
        str = str.substring(1, str.length() - 1);

        // Return the modified string
        return str;
    }

}
