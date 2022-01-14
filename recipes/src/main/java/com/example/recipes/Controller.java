package com.example.recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.example.recipes.authentication.User;
import com.example.recipes.authentication.UserDetailsUpd;
import com.example.recipes.authentication.UserRepository;

import java.util.*;

@RestController
@RequestMapping("/api/recipe")
public class Controller {
    private final RecipeService recipeService;
    private final UserRepository userRepository;

    @Autowired
    public Controller(RecipeService recipeService, UserRepository userRepository) {
        this.recipeService = recipeService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public RecipeDTO getRecipe(@PathVariable Long id) {
        return new RecipeDTO(recipeService.getRecipeById(id));
    }

    @PostMapping("/new")
    public Map<String, Long> saveRecipe(@AuthenticationPrincipal UserDetailsUpd userDetails, @Validated @RequestBody Recipe recipe) {
        Recipe recipeSave = new Recipe(recipe, getCurrentUser(userDetails));
        HashMap<String, Long> Id = new HashMap<>();
        Id.put("id", recipeService.saveRecipe(recipeSave).getId());
        return Id;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@AuthenticationPrincipal UserDetailsUpd userDetails, @PathVariable Long id){
        User user = recipeService.getRecipeById(id).getUser();
        if (!user.getEmail().equals(userDetails.getUsername()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<RecipeDTO> searchRecipes(@RequestParam(required = false) String name, @RequestParam(required = false) String category) {
        List<RecipeDTO> findRecipes = Collections.emptyList();
        if (category == null && name == null || category != null && name != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (category == null)
            findRecipes = recipeService.searchRecipesBy(name, 1);
        if (name == null)
            findRecipes = recipeService.searchRecipesBy(category, 2);
        findRecipes.forEach(System.out::println);
        return findRecipes;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecipe(@AuthenticationPrincipal UserDetailsUpd userDetails, @PathVariable Long id, @Validated @RequestBody Recipe recipe) {
        User user = getCurrentUser(userDetails);
        recipe = recipeService.updateRecipe(id, recipe);
        if (!user.getId().equals(recipe.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.noContent().build();
    }

    private User getCurrentUser(UserDetailsUpd userDetails) {
        Optional<User> userOpt = userRepository.findById(userDetails.getId());
        if (userOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return userOpt.get();
    }
}
