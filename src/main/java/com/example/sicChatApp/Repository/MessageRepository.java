package com.example.sicChatApp.Repository;

import com.example.sicChatApp.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT m From Message m JOIN m.chat c WHERE c.id = :chatId")
    public List<Message> findByChatId(@Param("chatId") Integer chatId);
}
