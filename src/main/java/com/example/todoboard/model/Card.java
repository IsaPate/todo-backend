package com.example.todoboard.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
// @Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Card {

    public Card(String title2, String description2, CardStatus status2, Integer position2, Board board2) {
        this.title = title2;
        this.description = description2;
        this.status = status2;
        this.position = position2;
        this.board = board2;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private CardStatus status;
    private Integer position;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;
}
