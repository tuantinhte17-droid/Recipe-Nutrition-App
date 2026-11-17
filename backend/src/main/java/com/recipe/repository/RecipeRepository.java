package com.recipe.repository;

import com.recipe.entity.Recipe;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByTitleContainingIgnoreCase(String title, Sort sort);
    List<Recipe> findByIdLessThanEqual(Long maxId, Sort sort);
    List<Recipe> findByIdLessThanEqualAndTitleContainingIgnoreCase(Long maxId, String title, Sort sort);
}
