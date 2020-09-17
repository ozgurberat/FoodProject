package com.ozgurberat.foodproject.model;

import com.google.gson.annotations.SerializedName;

public class Food {

    @SerializedName("idMeal")
    private String mealId;

    @SerializedName("strMeal")
    private String mealName;

    @SerializedName("strMealThumb")
    private String mealImage;

    public Food(String mealId, String mealName, String mealImage) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealImage = mealImage;
    }

    public String getMealId() {
        return mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public String getMealImage() {
        return mealImage;
    }

    @Override
    public String toString() {
        return "MealModel{" +
                "mealId='" + mealId + '\'' +
                ", mealName='" + mealName + '\'' +
                ", mealImage='" + mealImage + '\'' +
                '}';
    }
}
