package com.example.todoboard.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.todoboard.model.User;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);

    boolean existsByUsername(String username);
}
