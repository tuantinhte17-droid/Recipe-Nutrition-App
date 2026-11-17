package com.recipe.dto;
import java.time.LocalDateTime;

public record UserDto (
    Long id,
    String fullName,
    String email,
    LocalDateTime createdAt

) {}
