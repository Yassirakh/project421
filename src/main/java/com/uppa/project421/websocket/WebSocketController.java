package com.uppa.project421.websocket;

import org.json.JSONObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.*;

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

    @MessageMapping("/lobby.startCharge")
    @SendTo("/topic/lobby")
    public String startCharge(@Payload String usernames, SimpMessageHeaderAccessor headerAccessor) {
        usernames = usernames.concat("/startCharge");
        return usernames;
    }


    @MessageMapping("/lobby.startTourCharge")
    @SendTo("/topic/lobby")
    public String startTourCharge(@Payload String username, SimpMessageHeaderAccessor headerAccessor) {
        username = username.concat("/startTourCharge");
        return username;
    }

    @MessageMapping("/lobby.nextTourCharge")
    @SendTo("/topic/lobby")
    public String nextTourCharge(@Payload String lance_details, SimpMessageHeaderAccessor headerAccessor) {
        lance_details = lance_details.concat("/nextTourCharge");
        return lance_details;
    }


    @MessageMapping("/lobby.relanceDesDonnes")
    @SendTo("/topic/lobby")
    public String relanceDesDonnes(@Payload String minimum_players, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("TEEEEEEEEEST");
        System.out.println(minimum_players);
        List<String> minimum_players_array = new ArrayList<String>(Arrays.asList(minimum_players.split(",")));
        String partie_id =  minimum_players_array.get(minimum_players_array.size() - 1);
        System.out.println(partie_id);
        minimum_players_array.remove(minimum_players_array.size() - 1);
        minimum_players_array.forEach(m -> {
            System.out.println(m);
        });
        int size = minimum_players_array.size();
        Map<String, Integer> playerDes = new HashMap<String, Integer>();
        int i = 0;
        ArrayList numbers = new ArrayList();
        Random random = new Random();
        while (numbers.size() < size) {
            //Get Random numbers between range
            int randomNumber = random.nextInt((6 - 1) + 1) + 1;
            //Check for duplicate values
            if (!numbers.contains(randomNumber)) {
                numbers.add(randomNumber);
                playerDes.put(minimum_players_array.get(i), randomNumber);
                i++;
            }
        }
        JSONObject json = new JSONObject(playerDes);
        String res = json.toString().concat("/").concat(partie_id).concat("/relanceDesDonnes");
        System.out.println(res);
        return res;
    }

    @MessageMapping("/lobby.loserJeton")
    @SendTo("/topic/lobby")
    public String loserJeton(@Payload String loser, SimpMessageHeaderAccessor headerAccessor) {
        return loser.concat("/loserJeton");
    }

    @MessageMapping("/lobby.updateTokensUI")
    @SendTo("/topic/lobby")
    public String updateTokensUI(@Payload String loser, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("loser");
        System.out.println(loser);
        return loser.concat("/updateTokensUI");
    }

    @MessageMapping("/lobby.updateTokensUIAfterNenette")
    @SendTo("/topic/lobby")
    public String updateTokensUIAfterNenette(@Payload String loser, SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("loser nenette");
        System.out.println(loser);
        return loser.concat("/updateTokensUIAfterNenette");
    }

    @MessageMapping("/lobby.relanceDone")
    @SendTo("/topic/lobby")
    public String relanceDone(@Payload String partieId, SimpMessageHeaderAccessor headerAccessor) {
        return partieId.concat("/relanceDone");
    }
}
