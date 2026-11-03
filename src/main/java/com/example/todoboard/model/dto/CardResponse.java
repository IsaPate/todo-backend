package com.example.todoboard.model.dto;

import com.example.todoboard.model.Card;
import com.example.todoboard.model.CardStatus;

public record CardResponse(Integer id, String title, String description, CardStatus status, Integer position,
                Integer boardId) {

        public static CardResponse from(Card card) {
                return new CardResponse(card.getId(), card.getTitle(), card.getDescription(),
                                card.getStatus(), card.getPosition(), card.getBoard().getId());
        }
}
