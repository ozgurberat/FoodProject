package com.ozgurberat.foodproject.model;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "category_table")
public class Category {

    @PrimaryKey(autoGenerate = true)
    private int categoryPk;

    @SerializedName("strCategory")
    private String categoryName;

    @SerializedName("strCategoryThumb")
    private String categoryImage;

    public Category(String categoryName, String categoryImage) {
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }

    public int getCategoryPk() {
        return categoryPk;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryPk(int categoryPk) {
        this.categoryPk = categoryPk;
    }

    @Override
    public String toString() {
        return "CategoryModel{" +
                "categoryName='" + categoryName + '\'' +
                ", categoryImage='" + categoryImage + '\'' +
                '}';
    }
}
