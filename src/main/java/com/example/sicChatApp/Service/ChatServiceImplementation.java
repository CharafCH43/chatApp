package com.example.sicChatApp.Service;

import com.example.sicChatApp.Exception.ChatException;
import com.example.sicChatApp.Exception.UserException;
import com.example.sicChatApp.Model.Chat;
import com.example.sicChatApp.Model.User;
import com.example.sicChatApp.Repository.ChatRepository;
import com.example.sicChatApp.Request.GroupChatRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ChatServiceImplementation implements ChatService{

    private ChatRepository chatRepository ;
    private UserService userService ;
    @Override
    public Chat createChat(User reqUser, Integer userId2) throws UserException {

        User user = userService.findUserById(userId2);

        Chat isChatExist = chatRepository.findSingleChatByUserIds(user, reqUser);
        if(isChatExist != null) {
            return isChatExist;
        }
        Chat chat = new Chat();
        chat.setCreatedBy(reqUser);
        chat.getUsers().add(user);
        chat.getUsers().add(reqUser);
        chat.setGroup(false);
        chatRepository.save(chat);
        return chat;
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if(chat.isPresent()) {
            return chat.get();
        }
        throw new ChatException("Could not find chat with id " + chatId);

    }

    @Override
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException {

        User user = userService.findUserById(userId);
        List<Chat> chats = chatRepository.findChatByUserId(user.getId());

        return chats;
    }

    @Override
    public Chat createGroup(GroupChatRequest req, User reqUser) throws UserException {
        Chat group = new Chat();
        group.setGroup(true);
        group.setChat_image(req.getChat_image());
        group.setChat_name(req.getChat_name());
        group.setCreatedBy(reqUser);
        group.getAdmins().add(reqUser);
        for (Integer userId:
            req.getUserIds() ) {
            User user = userService.findUserById(userId);
            group.getUsers().add(user);
        }
        chatRepository.save(group);
        return group;
    }

    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException {
        Optional<Chat> opt = chatRepository.findById(chatId);
        User user = userService.findUserById(userId);
        if(opt.isPresent()) {
            Chat chat = opt.get();
            if(chat.getAdmins().contains(reqUser))
            {
                chat.getUsers().add(user);
                return chatRepository.save(chat);
            }
            else {
                throw new UserException("you are not allowed to add a user");
            }
        }
        throw new ChatException("chat not found with id " + chatId);
    }

    @Override
    public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws UserException, ChatException {
        Optional<Chat> opt = chatRepository.findById(chatId);
        if(opt.isPresent()) {
            Chat chat = opt.get();
            if(chat.getUsers().contains(reqUser)){
                chat.setChat_name(groupName);
                return chatRepository.save(chat);
            }
            throw new ChatException("you are not member of this group");
        }
        throw new ChatException("chat not found with id " + chatId);
    }

    @Override
    public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {
        Optional<Chat> opt = chatRepository.findById(chatId);
        User user = userService.findUserById(userId);
        if(opt.isPresent()) {
            Chat chat = opt.get();
            if(chat.getAdmins().contains(reqUser))
            {
                chat.getUsers().remove(user);
                return chatRepository.save(chat);
            }
            else if (chat.getUsers().contains(reqUser)) {
                if(user.getId().equals(reqUser.getId())){
                    chat.getUsers().remove(user);
                    return chatRepository.save(chat);
                }

            }

                throw new UserException("you can't remove a user");
        }
        throw new ChatException("chat not found with id " + chatId);
    }

    @Override
    public void deleteChat(Integer chatId, Integer userId) throws ChatException, UserException {
        Optional<Chat> opt = chatRepository.findById(chatId);
        if(opt.isPresent()) {
            Chat chat = opt.get();
            chatRepository.deleteById(chat.getId());
        }
    }
}
