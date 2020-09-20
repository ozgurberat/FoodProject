package com.ozgurberat.foodproject.requests;

import com.ozgurberat.foodproject.requests.responses.CategoryResponse;
import com.ozgurberat.foodproject.requests.responses.FoodResponse;
import com.ozgurberat.foodproject.requests.responses.RecipeResponse;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import kotlinx.coroutines.flow.Flow;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("categories.php")
    Flowable<CategoryResponse> getCategories();

    @GET("filter.php")
    Flowable<FoodResponse> getFoods(
            @Query("c") String category
    );

    @GET("lookup.php")
    Flowable<RecipeResponse> getFoodRecipe(
            @Query("i") String id
    );

    @GET("filter.php")
    Flowable<FoodResponse> getQueriedFoods(
            @Query("i") String query
    );

}
