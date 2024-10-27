package com.example.sicChatApp.Controller;

import com.example.sicChatApp.Model.Message;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
@Controller
@AllArgsConstructor
public class RealtimeChat {

    private  SimpMessagingTemplate messagingTemplate;

    // define methode for Room chat

    // methode for private chat
    @MessageMapping("/chat/{groupId}") // Maps to /app/chat/chatId
    public Message receiveMessage(@Payload Message message,
                                  @DestinationVariable String groupId) throws Exception {
        System.out.println("----------------------Received message "+message.getContent());
        // This method could handle additional logic before broadcasting if necessary
        messagingTemplate.convertAndSendToUser(groupId ,"/private", message);

        return message;
    }

    @MessageMapping("/notify/{userId}") // Maps to /app/chat/chatId
    public Message notify(@Payload Message message,
                                  @DestinationVariable String userId) throws Exception {

        System.out.println("----------------------Notifi message "+message.getContent());
        messagingTemplate.convertAndSendToUser(userId ,"/queue/private", message);
        return message;
    }


}

