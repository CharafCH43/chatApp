package com.example.sicChatApp.Service;

import com.example.sicChatApp.Exception.ChatException;
import com.example.sicChatApp.Exception.UserException;
import com.example.sicChatApp.Model.Chat;
import com.example.sicChatApp.Model.User;
import com.example.sicChatApp.Request.GroupChatRequest;

import java.util.List;

public interface ChatService {

    public Chat createChat(User reqUser, Integer userId2) throws UserException;
    public Chat findChatById(Integer chatId) throws ChatException;
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException;

    Chat createGroup(GroupChatRequest req, User reqUser) throws UserException;
    public Chat addUserToGroup(Integer userId, Integer chatId,User reqUser) throws UserException, ChatException;
    public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws UserException,ChatException;
    public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException,ChatException;
    public void deleteChat(Integer chatId, Integer userId) throws ChatException,UserException;
}
