package com.uppa.project421.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {



    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/publicChatRoom")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/publicChatRoom")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getUsername());
        headerAccessor.getSessionAttributes().put("userId", chatMessage.getUserId());
        //System.out.println(headerAccessor.getSessionAttributes().get("joueur").toString());
        return chatMessage;
    }


    @MessageMapping("/chat.addUserToPool")
    @SendTo("/topic/publicChatRoom")
    public ChatMessage addUserToPool(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getUsername());
        headerAccessor.getSessionAttributes().put("userId", chatMessage.getUserId());
        //System.out.println(headerAccessor.getSessionAttributes().get("joueur").toString());
        return chatMessage;
    }

    @MessageMapping("/lobby.sendInvite")
    @SendTo("/topic/lobby")
    public String sendInvite(@Payload String ids, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println(ids);
        ids = ids.concat("/inviteReceived");
        return ids;
    }


    @MessageMapping("/lobby.cancelInvite")
    @SendTo("/topic/lobby")
    public String cancelInvite(@Payload String ids, SimpMessageHeaderAccessor headerAccessor) {
        ids = ids.concat("/inviteCanceled");
        return ids;
    }

    @MessageMapping("/lobby.refuseInvite")
    @SendTo("/topic/lobby")
    public String refuseInvite(@Payload String id, SimpMessageHeaderAccessor headerAccessor) {
        id = id.concat("/inviteRefused");
        return id;
    }

    @MessageMapping("/lobby.acceptInvite")
    @SendTo("/topic/lobby")
    public String acceptInvite(@Payload String id, SimpMessageHeaderAccessor headerAccessor) {
        id = id.concat("/inviteAccepted");
        return id;
    }


    @MessageMapping("/lobby.gameLaunch")
    @SendTo("/topic/lobby")
    public String gameLaunch(@Payload String ids, SimpMessageHeaderAccessor headerAccessor) {
        ids = ids.concat("/gameLaunched");
        return ids;
    }

}
