package com.ozgurberat.foodproject.requests.responses;

import com.google.gson.annotations.SerializedName;
import com.ozgurberat.foodproject.model.Category;

import java.util.List;

public class CategoryResponse {

    @SerializedName("categories")
    private List<Category> categories;

    public CategoryResponse(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
