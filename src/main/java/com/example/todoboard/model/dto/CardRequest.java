package com.example.todoboard.model.dto;

import com.example.todoboard.model.CardStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CardRequest(
        @NotBlank String title, String description, @NotNull CardStatus status, @NotNull Integer position,
        @NotNull(message = "board is required") Integer boardId) {

}
