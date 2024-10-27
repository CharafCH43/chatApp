package com.example.sicChatApp.Controller;

import com.example.sicChatApp.Exception.ChatException;
import com.example.sicChatApp.Exception.MessageException;
import com.example.sicChatApp.Exception.UserException;
import com.example.sicChatApp.Model.Message;
import com.example.sicChatApp.Model.User;
import com.example.sicChatApp.Request.SendMessageRequest;
import com.example.sicChatApp.Response.ApiResponse;
import com.example.sicChatApp.Service.MessageService;
import com.example.sicChatApp.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessageService messageService;
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Message> sendMessageHandler(@RequestBody SendMessageRequest req, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {

        User user = userService.findUserProfile(jwt);
        req.setUserId(user.getId());
        Message message = messageService.sendMessage(req);

        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getChatsMessagesHandler(@PathVariable Integer chatId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {

        User user = userService.findUserProfile(jwt);
        System.out.println("getChatsMessagesHandler User Id --------------------------------"+user.getId());
        List<Message> messages = messageService.getChatsMessages(chatId,user);

        System.out.println("getChatsMessagesHandler--------------------------------"+messages.size());
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessageHandler(@PathVariable Integer messageId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException, MessageException {
        System.out.println("deleteMessageHandler--------------------------------");
        User user = userService.findUserProfile(jwt);
        messageService.deleteMessage(messageId,user);

        ApiResponse res = new ApiResponse("message deleted successfully", false);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}
