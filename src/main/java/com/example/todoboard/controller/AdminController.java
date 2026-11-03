package com.example.todoboard.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @DeleteMapping("/users/{id}")
    public void deleteUser() {
    }

    @DeleteMapping("/board/{id}")
    public void deleteBoard() {
    }
}
