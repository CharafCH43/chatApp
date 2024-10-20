package com.example.sicChatApp.Repository;

import com.example.sicChatApp.Model.Chat;
import com.example.sicChatApp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("SELECT c FROM Chat c JOIN c.users u WHERE u.id =:userId")
    public List<Chat> findChatByUserId(@Param("userId")Integer userId);

    @Query("SELECT c FROM Chat c WHERE c.isGroup=false AND :user Member of c.users AND :reqUser Member of c.users")
    public Chat findSingleChatByUserIds(@Param("user") User user, @Param("reqUser")User reqUser);
}
