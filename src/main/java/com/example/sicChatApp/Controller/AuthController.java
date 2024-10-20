package com.example.sicChatApp.Controller;



import com.example.sicChatApp.Configuration.TokenProvider;
import com.example.sicChatApp.Exception.UserException;
import com.example.sicChatApp.Model.User;
import com.example.sicChatApp.Repository.UserRepository;
import com.example.sicChatApp.Request.LoginRequest;
import com.example.sicChatApp.Response.AuthResponse;
import com.example.sicChatApp.Service.CustomUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder ;
    private TokenProvider tokenProvider;
    private CustomUserService customUserService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler (@Valid @RequestBody User user) throws UserException {
        System.out.println("user  : "+user.getUsername()+"\n"+user.getEmail()+"\n"+user.getPassword());
        String email = user.getEmail();
        String username = user.getUsername();
        String password = user.getPassword();

        User isUser = userRepository.findByEmail(email);
        if(isUser != null) {
            throw new UserException("Email "+email+" already used for another account");
        }

        User createdUser = new User() ;
        createdUser.setEmail(email);
        createdUser.setUsername(username);
        createdUser.setPassword(passwordEncoder.encode(password));

        userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        AuthResponse res = new AuthResponse(jwt, true);
        return new ResponseEntity<AuthResponse>(res, HttpStatus.ACCEPTED) ;
    }
 @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req){


        String email = req.getEmail();
        String password = req.getPassword();
        System.out.println("user login  : "+email+"\n"+password);
        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse(jwt, true);

        return new ResponseEntity<AuthResponse>(res, HttpStatus.ACCEPTED) ;
    }

    public Authentication authenticate(String username, String password){
        UserDetails userDetails = customUserService.loadUserByUsername(username);

        if(userDetails == null){
            throw new BadCredentialsException("invalid username");
        }
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("invalid password or username");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
