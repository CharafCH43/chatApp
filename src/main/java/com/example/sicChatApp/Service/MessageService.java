package com.example.sicChatApp.Service;

import com.example.sicChatApp.Exception.ChatException;
import com.example.sicChatApp.Exception.MessageException;
import com.example.sicChatApp.Exception.UserException;
import com.example.sicChatApp.Model.Message;
import com.example.sicChatApp.Model.User;
import com.example.sicChatApp.Request.SendMessageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MessageService {

    public Message sendMessage(SendMessageRequest req) throws UserException, ChatException;
    public List<Message> getChatsMessages(Integer chatId, User reqUser) throws ChatException, UserException;
    public Message findMessageById(Integer messageId) throws MessageException;
    public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException;

    // New method to mark a message as read
    // This method is added to the MessageService interface and implementation class

}
