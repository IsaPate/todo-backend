package com.example.todoboard.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todoboard.model.Card;
import com.example.todoboard.model.CardStatus;

@Repository
public interface CardRepo extends JpaRepository<Card, Integer> {
    List<Card> findByBoardUserId(int userId);

    Optional<Card> findByIdAndBoardUserId(int cardId, int userId);

    List<Card> findAllCardsByBoardId(int boardId);

    List<Card> findAllCardsByBoardIdAndStatus(int boardId, CardStatus status);
}
