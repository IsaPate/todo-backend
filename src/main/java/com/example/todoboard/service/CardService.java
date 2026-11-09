package com.example.todoboard.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.todoboard.model.Board;
import com.example.todoboard.model.Card;
import com.example.todoboard.model.User;
import com.example.todoboard.model.dto.CardPatchRequest;
import com.example.todoboard.model.dto.CardRequest;
import com.example.todoboard.model.dto.CardResponse;
import com.example.todoboard.model.principal.UserPrincipal;
import com.example.todoboard.repo.CardRepo;
import com.example.todoboard.repo.UserRepo;

import jakarta.validation.Valid;

@Service
public class CardService {

    @Autowired
    private CardRepo cardRepo;

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserRepo userRepo;

    private Card findCard(int id) {
        return this.cardRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found"));
    }

    private User getUserOrThrow(UserPrincipal userPrincipal) {
        return userRepo.findById(userPrincipal.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));
    }

    public List<CardResponse> getAllCards(UserPrincipal userPrincipal) {
        User user = getUserOrThrow(userPrincipal);

        List<Card> cards = this.cardRepo.findByBoardUserId(user.getId());
        List<CardResponse> cardsResponse = cards.stream()
                .map(CardResponse::from)
                .collect(Collectors.toList());
        return cardsResponse;
    }

    public CardResponse findCardById(int id, UserPrincipal userPrincipal) {
        User user = getUserOrThrow(userPrincipal);
        Card card = this.cardRepo.findByIdAndBoardUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "card not found"));
        return CardResponse.from(card);
    }

    public CardResponse create(CardRequest request, UserPrincipal userPrincipal) {
        User user = getUserOrThrow(userPrincipal);
        Board board = this.boardService.getEntityBoard(request.boardId(), user.getId());
        Card card2 = new Card(
                request.title(), request.description(), request.status(), request.position(), board);
        Card saved = this.cardRepo.save(card2);
        return CardResponse.from(saved);
    }

    public void delete(int id, UserPrincipal userPrincipal) {
        User user = getUserOrThrow(userPrincipal);
        Card card = this.cardRepo.findByIdAndBoardUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "card not found"));
        this.cardRepo.deleteById(card.getId());
    }

    @Transactional
    public CardResponse update(int id, CardRequest cardRequest, UserPrincipal userPrincipal) {
        User user = getUserOrThrow(userPrincipal);

        Card card = this.cardRepo.findByIdAndBoardUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "card not found"));
        card.setTitle(cardRequest.title());
        card.setDescription(cardRequest.description());
        card.setStatus(cardRequest.status());
        card.setPosition(cardRequest.position());

        int boardId = cardRequest.boardId();

        Board fromDb = this.boardService.getEntityBoard(boardId, user.getId());

        card.setBoard(fromDb);

        return CardResponse.from(card);
    }

    public CardResponse updateCardByProperties(int id, CardPatchRequest cardPatchRequest,
            UserPrincipal userPrincipal) {

        User user = getUserOrThrow(userPrincipal);

        Card card = cardRepo.findByIdAndBoardUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card not found"));

        card.setPosition(cardPatchRequest.position());
        card.setStatus(cardPatchRequest.status());
        return CardResponse.from(card);

    }
}
