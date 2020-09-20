package com.ozgurberat.foodproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ozgurberat.foodproject.model.Recipe;
import com.ozgurberat.foodproject.repositories.RecipeRepository;
import com.ozgurberat.foodproject.requests.responses.RecipeResponse;


public class RecipeViewModel extends AndroidViewModel {

    private RecipeRepository repository;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        repository = RecipeRepository.getInstance(application);
    }

    public void fetchRecipe(String id) {
        repository.fetchRecipe(id);
    }

    public void fetchRecipeFromSQLite(String id) {
        repository.fetchRecipeFromSQLite(id);
    }

    public void triggerGetRecipe(RecipeResponse recipeResponse) {
        repository.triggerGetRecipe(recipeResponse);
    }

    public LiveData<RecipeResponse> getRecipe() {
        return repository.getRecipe();
    }

    public LiveData<Boolean> getIsLoading() {
        return repository.getIsLoading();
    }

    public LiveData<Boolean> getIsTimedOut() {
        return repository.getIsTimedOut();
    }

    public LiveData<Recipe> getRecipeFromSQLite() {
        return repository.getRecipeFromSQLite();
    }

}
