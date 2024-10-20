package com.example.sicChatApp.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class AuthResponse {
    @Getter
    private String jwt ;

    public boolean isAuth() {
        return isAuth;
    }

    private boolean isAuth ;
}
