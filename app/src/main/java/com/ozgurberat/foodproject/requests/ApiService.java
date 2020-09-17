package com.ozgurberat.foodproject.requests;

import com.ozgurberat.foodproject.requests.responses.CategoryResponse;
import com.ozgurberat.foodproject.requests.responses.FoodResponse;
import com.ozgurberat.foodproject.requests.responses.RecipeResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("categories.php")
    Observable<CategoryResponse> getCategories();

    @GET("filter.php")
    Observable<FoodResponse> getFoods(
            @Query("c") String category
    );

    @GET("lookup.php")
    Observable<RecipeResponse> getFoodRecipe(
            @Query("i") String id
    );

    @GET("filter.php")
    Observable<FoodResponse> getQueriedFoods(
            @Query("i") String query
    );

}
