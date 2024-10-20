package com.example.sicChatApp.Service;

import com.example.sicChatApp.Configuration.TokenProvider;
import com.example.sicChatApp.Exception.UserException;
import com.example.sicChatApp.Model.User;
import com.example.sicChatApp.Repository.UserRepository;
import com.example.sicChatApp.Request.UpdateUserRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImplementation implements UserService{

    private UserRepository userRepository ;
    private TokenProvider tokenProvider ;
    @Override
    public User findUserById(Integer id) throws UserException {
       Optional<User> opt = userRepository.findById(id) ;

       if(opt.isPresent())
       {
           return opt.get();
       }
        throw  new UserException("User not found with id " + id) ;
    }

    @Override
    public User findUserProfile(String jwt) throws UserException {
        String email = tokenProvider.getEmailFromToken(jwt) ;

        if(email == null) {
           throw new BadCredentialsException("received Invalid token---") ;
        }
        User user = userRepository.findByEmail(email) ;

        if (user == null) {
            throw new UserException("Couldn't find user with email' "+email) ;
        }

        return user;
    }

    @Override
    public User updateUser(Integer userId, UpdateUserRequest req) throws UserException {

        User user = findUserById(userId);
        if(req.getUsername() != null) {
            user.setUsername(req.getUsername());
        }
        if(req.getProfile_picture() != null) {
            user.setProfile_picture(req.getProfile_picture());
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> searchUser(String query) {

        List<User> users = userRepository.searchUser(query) ;
        return users;
    }
}
