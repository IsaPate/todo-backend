package com.example.todoboard.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todoboard.model.dto.auth.LoginResponse;
import com.example.todoboard.model.dto.auth.UserRequest;
import com.example.todoboard.model.dto.auth.UserResponse;
import com.example.todoboard.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequest userRequest) {
        // TODO: process POST request

        return new ResponseEntity<>(authService.register(userRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(authService.login(userRequest), HttpStatus.OK);
    }
}
