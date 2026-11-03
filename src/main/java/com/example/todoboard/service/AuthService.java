package com.example.todoboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.todoboard.model.User;
import com.example.todoboard.model.dto.auth.LoginResponse;
import com.example.todoboard.model.dto.auth.UserRequest;
import com.example.todoboard.model.dto.auth.UserResponse;
import com.example.todoboard.repo.BoardRepo;
import com.example.todoboard.repo.UserRepo;

@Service
public class AuthService {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BoardRepo boardRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse register(UserRequest userRequest) {

        if (userRepo.existsByUsername(userRequest.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");

        }

        String hashed = this.passwordEncoder.encode(userRequest.password());

        User saved = this.userRepo.save(new User(
                userRequest.username(), hashed,
                userRequest.userRole()));

        return UserResponse.from(saved);

    }

    public LoginResponse login(UserRequest userRequest) {

        if (!userRepo.existsByUsername(userRequest.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Need to register first");
        }

        User userDb = userRepo.findUserByUsername(userRequest.username()).orElseThrow(null);

        String hashed = userDb.getPassword();

        // String userReqPassHashed =
        // this.passwordEncoder.encode(userRequest.password());

        if (!this.passwordEncoder.matches(userRequest.password(), hashed)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Password do not match");
        }

        String token = jwtService.generateToken(userDb.getUsername());
        return new LoginResponse(token, UserResponse.from(userDb));

    }
}
