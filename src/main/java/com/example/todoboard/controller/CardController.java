package com.example.todoboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;

import com.example.todoboard.model.dto.CardRequest;
import com.example.todoboard.model.dto.CardResponse;
import com.example.todoboard.model.principal.UserPrincipal;
import com.example.todoboard.service.CardService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    @GetMapping("/cards")
    public ResponseEntity<List<CardResponse>> getCards(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<CardResponse> cards = this.cardService.getAllCards(userPrincipal);
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @GetMapping("/card/{id}")
    public ResponseEntity<CardResponse> getCard(@PathVariable int id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new ResponseEntity<>(this.cardService.findCardById(id, userPrincipal), HttpStatus.OK);
    }

    @PostMapping("/card")
    public ResponseEntity<CardResponse> createCard(@Valid @RequestBody CardRequest card,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        return new ResponseEntity<>(this.cardService
                .create(card, userPrincipal),
                HttpStatus.CREATED);
    }

    @PutMapping("/card/{id}")
    public ResponseEntity<CardResponse> updateCard(@PathVariable int id, @Valid @RequestBody CardRequest cardRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new ResponseEntity<>(this.cardService.update(id, cardRequest, userPrincipal), HttpStatus.OK);
    }

    @DeleteMapping("/card/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(@PathVariable int id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        this.cardService.delete(id, userPrincipal);
    }
}
