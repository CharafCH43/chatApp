package com.example.sicChatApp.Service;

import com.example.sicChatApp.Exception.UserException;
import com.example.sicChatApp.Model.User;
import com.example.sicChatApp.Request.UpdateUserRequest;

import java.util.List;

public interface UserService {
    public User findUserById(Integer id) throws UserException;
    public User findUserProfile(String jwt) throws UserException;

   public User updateUser(Integer userId, UpdateUserRequest req) throws UserException;

    public List<User> searchUser (String query) ;
}
