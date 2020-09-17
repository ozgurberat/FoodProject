package com.ozgurberat.foodproject.model;

import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("strCategory")
    private String categoryName;

    @SerializedName("strCategoryThumb")
    private String categoryImage;

    public Category(String categoryName, String categoryImage, String categoryDescription) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }


    @Override
    public String toString() {
        return "CategoryModel{" +
                "categoryName='" + categoryName + '\'' +
                ", categoryImage='" + categoryImage + '\'' +
                '}';
    }
}
