package com.example.todoboard.model.dto.board;

import java.util.List;
import java.util.stream.Collectors;

import com.example.todoboard.model.Board;
import com.example.todoboard.model.Card;
import com.example.todoboard.model.dto.CardResponse;

public record BoardResponse(
        Integer id, String name, List<CardResponse> cards) {

    public static BoardResponse from(Board board) {
        return new BoardResponse(
                board.getId(), board.getName(),
                board.getCards().stream()
                        .map(CardResponse::from)
                        .collect(Collectors.toList()));
    }
}
