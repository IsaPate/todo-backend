package com.example.todoboard.model.dto.auth;

import com.example.todoboard.model.User;
import com.example.todoboard.model.UserRole;

public record UserResponse(Integer id, String username, UserRole userRole) {

    public static UserResponse from(User saved) {
        return new UserResponse(saved.getId(), saved.getUsername(), saved.getUserRole());
    }
}
