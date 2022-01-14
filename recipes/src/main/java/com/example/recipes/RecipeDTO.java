package com.example.recipes;

import java.sql.Timestamp;
import java.util.List;

public class RecipeDTO {
    private String name;
    private String description;
    private String category;
    private Timestamp date;
    private List<String> ingredients;
    private List<String> directions;

    private RecipeDTO() {
    }

    public RecipeDTO(Recipe recipe) {
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.category = recipe.getCategory();
        if (recipe.getUpdateDate() == null)
            this.date = recipe.getCreateDate();
        else
            this.date = recipe.getUpdateDate();
        this.ingredients = recipe.getIngredients();
        this.directions = recipe.getDirections();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getDirections() {
        return directions;
    }

    public Timestamp getDate() {
        return date;
    }
}
