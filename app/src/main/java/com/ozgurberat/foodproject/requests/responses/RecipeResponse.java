package com.ozgurberat.foodproject.requests.responses;

import com.google.gson.annotations.SerializedName;
import com.ozgurberat.foodproject.model.Recipe;

import java.util.List;

public class RecipeResponse {

    @SerializedName("meals")
    private List<Recipe> recipe;

    public RecipeResponse(List<Recipe> recipe) {
        this.recipe = recipe;
    }

    public List<Recipe> getRecipe() {
        return recipe;
    }
}
