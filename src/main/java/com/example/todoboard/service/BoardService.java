package com.example.todoboard.service;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.query.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.todoboard.model.Board;
import com.example.todoboard.model.User;
import com.example.todoboard.model.dto.board.BoardRequest;
import com.example.todoboard.model.dto.board.BoardResponse;
import com.example.todoboard.model.principal.UserPrincipal;
import com.example.todoboard.repo.BoardRepo;
import com.example.todoboard.repo.UserRepo;

import jakarta.persistence.OrderBy;

@Service
public class BoardService {
    @Autowired
    private BoardRepo boardRepo;

    @Autowired
    private UserRepo userRepo;

    private User getUserOrThrow(UserPrincipal userPrincipal) {
        return userRepo.findById(userPrincipal.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));

    }

    public Board getEntityBoard(int id, int userId) {
        return this.boardRepo.findBoardByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found"));
    }

    @Transactional(readOnly = true)
    public BoardResponse getBoardById(int id, UserPrincipal userPrincipal) {
        User user = getUserOrThrow(userPrincipal);

        Board board = this.getEntityBoard(id, user.getId());
        return BoardResponse.from(board);
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> getBoards(UserPrincipal userPrincipal) {

        User user = getUserOrThrow(userPrincipal);

        List<Board> boards = this.boardRepo.findBoardsByUserId(user.getId());
        // Sort.by(Sort.Direction.ASC, "id"
        return boards.stream()
                .map(BoardResponse::from)
                .collect(Collectors.toList());
    }

    public BoardResponse create(BoardRequest boardRequest, UserPrincipal userPrincipal) {
        User user = getUserOrThrow(userPrincipal);

        Board board = new Board();
        board.setName(boardRequest.name());
        board.setUser(user);
        Board saved = this.boardRepo.save(board);
        return BoardResponse.from(saved);
    }

    @Transactional
    public BoardResponse update(int id, BoardRequest boardRequest, UserPrincipal userPrincipal) {
        User user = getUserOrThrow(userPrincipal);

        Board finded = this.getEntityBoard(id, user.getId());
        finded.setName(boardRequest.name());
        // Board updated = this.boardRepo.save(finded);
        return BoardResponse.from(finded);
    }

    public void delete(int id, UserPrincipal userPrincipal) {
        User user = getUserOrThrow(userPrincipal);

        Board board = this.getEntityBoard(id, user.getId());
        this.boardRepo.delete(board);
    }

}
