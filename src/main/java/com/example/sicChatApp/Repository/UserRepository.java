package com.example.sicChatApp.Repository;


import com.example.sicChatApp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.username like %:query% ")
    public List<User> searchUser(@Param("query") String query) ;
}
