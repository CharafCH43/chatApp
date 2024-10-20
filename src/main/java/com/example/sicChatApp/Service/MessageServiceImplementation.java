package com.example.sicChatApp.Service;

import com.example.sicChatApp.Exception.ChatException;
import com.example.sicChatApp.Exception.MessageException;
import com.example.sicChatApp.Exception.UserException;
import com.example.sicChatApp.Model.Chat;
import com.example.sicChatApp.Model.Message;
import com.example.sicChatApp.Model.User;
import com.example.sicChatApp.Repository.MessageRepository;
import com.example.sicChatApp.Request.SendMessageRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.MediaSize;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class MessageServiceImplementation implements MessageService{
    private MessageRepository messageRepository;
    private UserService userService;
    private ChatService chatService;
    @Override
    public Message sendMessage(SendMessageRequest req) throws UserException, ChatException {
        User user = userService.findUserById(req.getUserId());
        Chat chat = chatService.findChatById(req.getChatId());

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(req.getContent());
        message.setTimestamp(LocalDateTime.now());
        return message ;
    }

    @Override
    public List<Message> getChatsMessages(Integer chatId, User reqUser) throws ChatException,UserException {
        Chat chat = chatService.findChatById(chatId);

        if(!chat.getUsers().contains(reqUser))
        {
            throw new UserException("you are not related the chat "+chat.getId());
        }
        List<Message> messages = messageRepository.findByChatId(chat.getId());
        return messages;
    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {

        Optional<Message> opt = messageRepository.findById(messageId);

        if(opt.isPresent()) {
            return opt.get();
        }
        throw new MessageException("message not found with id " + messageId);
    }

    @Override
    public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException {

        Message message = findMessageById(messageId);
        if(message.getUser().getId().equals(reqUser.getId())){
            messageRepository.deleteById(messageId);
        }
        throw new UserException("you can not delete another user's message "+reqUser.getUsername());
    }
}
