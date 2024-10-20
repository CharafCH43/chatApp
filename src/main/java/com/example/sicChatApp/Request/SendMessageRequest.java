package com.example.sicChatApp.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SendMessageRequest {

    private Integer userId;
    private Integer chatId;
    private String content ;
}
