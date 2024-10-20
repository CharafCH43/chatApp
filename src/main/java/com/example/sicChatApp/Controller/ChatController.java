package com.example.sicChatApp.Controller;

import com.example.sicChatApp.Exception.ChatException;
import com.example.sicChatApp.Exception.UserException;
import com.example.sicChatApp.Model.Chat;
import com.example.sicChatApp.Model.User;
import com.example.sicChatApp.Request.GroupChatRequest;
import com.example.sicChatApp.Request.SingleChatRequest;
import com.example.sicChatApp.Response.ApiResponse;
import com.example.sicChatApp.Service.ChatService;
import com.example.sicChatApp.Service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private ChatService chatService;
    private UserService userService;

    @PostMapping("/private")
    public ResponseEntity<Chat> createChatHandler(@RequestBody SingleChatRequest singleChatRequest, @RequestHeader("Authorization") String jwt) throws UserException {
        System.out.println(singleChatRequest.getUserId() + ": " + jwt);
        System.out.println("create chat ------------------------------");
        User reqUser = userService.findUserProfile(jwt);

        Chat chat = chatService.createChat(reqUser, singleChatRequest.getUserId());
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @PostMapping("/room")
    public ResponseEntity<Chat> createRoomHandler(@RequestBody GroupChatRequest req, @RequestHeader("Authorization") String jwt) throws UserException {
        System.out.println("createRoom--------------------------------");
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.createGroup(req, reqUser);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatByIdHandler(@PathVariable Integer chatId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        Chat chat = chatService.findChatById(chatId);
        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Chat>> findAllChatByUserIdHandler( @RequestHeader("Authorization") String jwt) throws UserException {
        System.out.println("Find all chat by user id " + jwt);
        User reqUser = userService.findUserProfile(jwt);
        List<Chat> chats = chatService.findAllChatByUserId(reqUser.getId());
        return new ResponseEntity<List<Chat>>(chats, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToRoomHandler(@PathVariable Integer chatId,@PathVariable Integer userId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {

        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.addUserToGroup(userId,chatId,reqUser);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> removeUserFromRoomHandler(@PathVariable Integer chatId,@PathVariable Integer userId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.removeFromGroup(userId,chatId,reqUser);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ApiResponse> deleteChatHandler(@PathVariable Integer chatId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(jwt);
        chatService.deleteChat(chatId,reqUser.getId());
        ApiResponse res = new ApiResponse("chat is deleted successfully", false);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
