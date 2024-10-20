package com.example.sicChatApp.Controller;

import com.example.sicChatApp.Exception.UserException;
import com.example.sicChatApp.Model.User;
import com.example.sicChatApp.Request.UpdateUserRequest;
import com.example.sicChatApp.Response.ApiResponse;
import com.example.sicChatApp.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String token) throws UserException {
        System.out.println("profile--------------------");
        User user = userService.findUserProfile(token);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/query")
    public ResponseEntity<List<User>> searchUserHandler(@RequestParam("q") String q) {
        System.out.println("Searching user for " + q);
        List<User> users = userService.searchUser(q);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserHandler(@RequestBody UpdateUserRequest req, @RequestHeader("Authorization") String token) throws UserException {
        System.out.println("update user :"+req);
        User user = userService.findUserProfile(token);
        userService.updateUser(user.getId(), req);
        ApiResponse res = new ApiResponse("User updated successfully", true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
