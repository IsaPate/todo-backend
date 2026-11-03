package com.example.todoboard.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.todoboard.model.Board;

@Repository
public interface BoardRepo extends JpaRepository<Board, Integer> {

    List<Board> findBoardsByUserId(Integer userId);

    Optional<Board> findBoardByIdAndUserId(Integer boardId, Integer userId);
}
