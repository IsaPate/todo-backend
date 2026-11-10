package com.example.todoboard.service;

import java.util.List;
import java.util.stream.Collector;
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

    private List<Card> sortCardsByPosition(List<Card> cards) {
        return cards.stream().sorted((c1, c2) -> c1.getPosition().compareTo(c2.getPosition()))
                .collect(Collectors.toList());
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

        List<Card> cards = this.cardRepo.findAllCardsByBoardIdAndStatus(board.getId(), request.status());

        List<Card> sorted = sortCardsByPosition(cards);

        int index = Math.min(request.position(), sorted.size());

        sorted.add(index, card2);

        for (int i = 0; i < sorted.size(); i++) {
            sorted.get(i).setPosition(i);
        }

        Card saved = this.cardRepo.save(card2);

        return CardResponse.from(saved);
    }

    public void delete(int id, UserPrincipal userPrincipal) {
        User user = getUserOrThrow(userPrincipal);
        Card card = this.cardRepo.findByIdAndBoardUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "card not found"));
        this.cardRepo.deleteById(card.getId());
    }

    private Card moveCards(Card card, User user, CardRequest cardRequest) {

        List<Card> currentCards = this.cardRepo.findAllCardsByBoardIdAndStatus(card.getBoard().getId(),
                card.getStatus());

        if (!card.getStatus().equals(cardRequest.status())) {
            List<Card> listToBeMovedWithNewStatus = this.cardRepo.findAllCardsByBoardIdAndStatus(
                    card.getBoard().getId(),
                    cardRequest.status());
            List<Card> sortedListToBeMovedWithNewStatus = sortCardsByPosition(listToBeMovedWithNewStatus);
            currentCards.remove(card);
            int newIndex = Math.min(cardRequest.position(), sortedListToBeMovedWithNewStatus.size());
            sortedListToBeMovedWithNewStatus.add(newIndex, card);
            for (int i = 0; i < sortedListToBeMovedWithNewStatus.size(); i++) {

                sortedListToBeMovedWithNewStatus.get(i).setStatus(cardRequest.status());
                sortedListToBeMovedWithNewStatus.get(i).setPosition(i);

            }
        } else {
            if (!card.getPosition().equals(cardRequest.position())) {

                List<Card> sort = sortCardsByPosition(currentCards);
                // cards.removeIf( (c) ->c. );
                sort.remove(card);
                int index = Math.min(cardRequest.position(), sort.size());
                // if zero an iwant to move it to 2?
                sort.add(index, card);
                for (int i = 0; i < sort.size(); i++) {
                    sort.get(i).setPosition(i);
                }

            }
        }

        return card;
    }

    @Transactional
    public CardResponse update(int id, CardRequest cardRequest, UserPrincipal userPrincipal) {
        User user = getUserOrThrow(userPrincipal);

        Card card = this.cardRepo.findByIdAndBoardUserId(id, user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "card not found"));

        card.setTitle(cardRequest.title());
        card.setDescription(cardRequest.description());
        int boardId = cardRequest.boardId();

        Board fromDb = this.boardService.getEntityBoard(boardId, user.getId());

        card.setBoard(fromDb);

        if (!cardRequest.position().equals(card.getPosition()) || !cardRequest.status().equals(card.getStatus())) {
            Card moved = moveCards(card, user, cardRequest);
            return CardResponse.from(moved);
        }

        card.setStatus(cardRequest.status());
        card.setPosition(cardRequest.position());

        return CardResponse.from(card);
    }

    @Transactional
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
