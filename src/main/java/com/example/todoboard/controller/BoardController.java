package com.example.todoboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.todoboard.model.User;
import com.example.todoboard.model.dto.board.BoardRequest;
import com.example.todoboard.model.dto.board.BoardResponse;
import com.example.todoboard.model.principal.UserPrincipal;
import com.example.todoboard.service.BoardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/{id}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable int id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new ResponseEntity<>(this.boardService.getBoardById(id, userPrincipal), HttpStatus.OK);
    }

    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponse>> getAllBoards(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new ResponseEntity<>(this.boardService.getBoards(userPrincipal), HttpStatus.OK);
    }

    @PostMapping("/board")
    public ResponseEntity<BoardResponse> createBoard(@Valid @RequestBody BoardRequest boardRequest,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        System.out.println(boardRequest);
        return new ResponseEntity<>(this.boardService.create(boardRequest, userPrincipal), HttpStatus.CREATED);
    }

    @PutMapping("/board/{id}")
    public ResponseEntity<BoardResponse> updateBoard(@PathVariable int id,
            @Valid @RequestBody BoardRequest boardRequest, @AuthenticationPrincipal UserPrincipal userPrincipal) {

        return new ResponseEntity<>(this.boardService.update(id, boardRequest, userPrincipal), HttpStatus.OK);
    }

    @DeleteMapping("/board/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBoard(@PathVariable int id, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        this.boardService.delete(id, userPrincipal);
    }
}
