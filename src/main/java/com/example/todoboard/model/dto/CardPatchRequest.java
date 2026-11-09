package com.example.todoboard.model.dto;

import com.example.todoboard.model.CardStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CardPatchRequest(@NotNull @PositiveOrZero Integer position, @NotNull CardStatus status) {

}
