package com.ozgurberat.foodproject.requests.responses;

import com.google.gson.annotations.SerializedName;
import com.ozgurberat.foodproject.model.Food;

import java.util.List;

public class FoodResponse {

    @SerializedName("meals")
    private List<Food> foods;

    public FoodResponse(List<Food> foods) {
        this.foods = foods;
    }

    public List<Food> getFoods() {
        return foods;
    }
}
