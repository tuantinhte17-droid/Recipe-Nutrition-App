package com.recipe.controller;

import com.recipe.dto.FavoriteRecipeDto;
import com.recipe.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @GetMapping
    public ResponseEntity<List<FavoriteRecipeDto>> getUserFavorites(@RequestParam Long userId) {
        List<FavoriteRecipeDto> response = favoriteService.getUserFavorites(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{recipeId}")
    public ResponseEntity<?> addFavorite(@PathVariable Long recipeId,
                                         @RequestParam Long userId) {
        boolean isNewlyAdded = favoriteService.addFavorite(recipeId, userId);

        if (isNewlyAdded) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Đã thêm vào yêu thích"));
        } else {
            return ResponseEntity.ok(Map.of("message", "Món ăn đã có trong danh sách yêu thích"));
        }
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Long recipeId, @RequestParam Long userId) {
        favoriteService.removeFavorite(recipeId, userId);
        return ResponseEntity.ok(Map.of("message", "Đã xóa khỏi yêu thích"));
    }
}
