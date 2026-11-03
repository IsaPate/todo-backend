package com.example.todoboard.model.dto.board;

import jakarta.validation.constraints.NotBlank;

public record BoardRequest(@NotBlank(message = "board name required.") String name) {

}
