package com.example.messenger.message;

import com.example.template.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
            select u from User u
            where u.username = :name
            """)
    Optional<User> findByName(String name);
}
