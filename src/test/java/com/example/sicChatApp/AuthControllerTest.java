package com.example.sicChatApp;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.sicChatApp.Configuration.TokenProvider;
import com.example.sicChatApp.Controller.AuthController;
import com.example.sicChatApp.Model.User;
import com.example.sicChatApp.Repository.UserRepository;
import com.example.sicChatApp.Service.CustomUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private CustomUserService customUserService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testSignupSuccess() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("password123");

        // Mock repository to simulate no user with that email exists
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        // Mock password encoder and token generation
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(tokenProvider.generateToken(any(Authentication.class))).thenReturn("fakeJwtToken");

        // Mock saving user
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Mock authentication service
        when(customUserService.loadUserByUsername(anyString())).thenReturn((UserDetails) user);

        // Send POST request
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user))) // Convert user object to JSON string
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.token").value("fakeJwtToken"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void testSignupEmailAlreadyExists() throws Exception {
        User user = new User();
        user.setEmail("existing@example.com");
        user.setUsername("existinguser");
        user.setPassword("password123");

        // Mock userRepository to simulate a user with that email already exists
        when(userRepository.findByEmail("existing@example.com")).thenReturn(new User());

        // Send POST request and expect an error
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }
}

