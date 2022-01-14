package com.example.recipes;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.example.recipes.authentication.User;

import javax.persistence.*;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column
    private String name;

    @NotBlank
    @Column
    private String description;

    @NotBlank
    @Column
    private String category;

    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp createDate;

    @Column
    @UpdateTimestamp
    private Timestamp updateDate;

    @NotEmpty
    @Size(min = 1)
    @ElementCollection
    private List<String> ingredients;

    @NotEmpty
    @Size(min = 1)
    @ElementCollection
    private List<String> directions;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Recipe() {
    }

    public Recipe(String description, String name, String category, Timestamp createDate, Timestamp updateDate, List<String> ingredients, List<String> directions, User user) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.ingredients = ingredients;
        this.directions = directions;
        this.user = user;
    }

    public Recipe(Recipe recipeJSON, User user) {
        this.createDate = Timestamp.valueOf(LocalDateTime.now());
        this.updateDate = createDate;
        this.name = recipeJSON.getName();
        this.description = recipeJSON.getDescription();
        this.category = recipeJSON.category;
        this.ingredients = recipeJSON.getIngredients();
        this.directions = recipeJSON.getDirections();
        this.user = user;
    }

    public Recipe(Recipe recipeJSON, Recipe recipeEntity) {
        this.id = recipeEntity.getId();
        this.user = recipeEntity.getUser();
        this.createDate = recipeEntity.getCreateDate();
        this.updateDate = Timestamp.valueOf(LocalDateTime.now());
        this.name = recipeJSON.getName();
        this.description = recipeJSON.getDescription();
        this.category = recipeJSON.category;
        this.ingredients = recipeJSON.getIngredients();
        this.directions = recipeJSON.getDirections();
    }

    public Long getId() {
        return id;
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

    public Timestamp getCreateDate() {
        return createDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", ingredients=" + ingredients +
                ", directions=" + directions +
                '}';
    }
}
