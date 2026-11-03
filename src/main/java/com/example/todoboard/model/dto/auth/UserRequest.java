package com.example.todoboard.model.dto.auth;

import com.example.todoboard.model.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
                @NotBlank(message = "Username required") String username,
                @NotBlank(message = "Password required") @Size(min = 8, max = 72, message = "Too small password") String password,
                UserRole userRole) {

}
