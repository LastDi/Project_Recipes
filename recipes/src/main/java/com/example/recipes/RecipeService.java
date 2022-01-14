package com.example.recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private List<Recipe> allFindRecipes;
    private List<RecipeDTO> findRecipes;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
        allFindRecipes = Collections.emptyList();
        findRecipes = Collections.emptyList();
    }

    public Recipe getRecipeById(Long id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isPresent()) {
            return recipe.get();
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        if (recipeRepository.findById(id).isPresent())
            recipeRepository.deleteById(id);
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Recipe updateRecipe(Long id, Recipe recipe) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (optionalRecipe.isPresent()) {
            return recipeRepository.save(new Recipe(recipe, optionalRecipe.get()));
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public List<RecipeDTO> searchRecipesBy(String search, int identification) {
        switch (identification) {
            case 1 :
                allFindRecipes = recipeRepository.findByName(search);
                break;
            case 2 :
                allFindRecipes = recipeRepository.findByCategory(search);
                break;
        }
        convertAndSortFindCollections();
        return findRecipes;
    }

    private void convertAndSortFindCollections() {
        findRecipes = new ArrayList<>();
        for (Recipe recipe : allFindRecipes) {
            findRecipes.add(new RecipeDTO(recipe));
        }
        findRecipes.sort((o1, o2) -> (int) (o2.getDate().getTime() - o1.getDate().getTime()));
    }
}
