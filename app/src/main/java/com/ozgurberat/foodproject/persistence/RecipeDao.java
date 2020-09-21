package com.ozgurberat.foodproject.persistence;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ozgurberat.foodproject.model.Recipe;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface RecipeDao {

    @Insert(entity = Recipe.class, onConflict = OnConflictStrategy.REPLACE)
    Maybe<Long> insertRecipe(Recipe recipe);

    @Delete(entity = Recipe.class)
    Maybe<Integer> deleteRecipe(Recipe recipe);

    @Query("SELECT * FROM recipe_table WHERE mealId == :mealId")
    Flowable<Recipe> getRecipe(String mealId);

    @Query("SELECT * FROM recipe_table")
    Flowable<List<Recipe>> getAllRecipes();

    @Query("DELETE FROM recipe_table")
    Maybe<Integer> deleteAllRecipes();

}







