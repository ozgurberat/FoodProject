package com.ozgurberat.foodproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ozgurberat.foodproject.model.Recipe;
import com.ozgurberat.foodproject.repositories.SavedRecipeRepository;

import java.util.List;

public class SavedRecipeViewModel extends AndroidViewModel {

    private SavedRecipeRepository repository;

    public SavedRecipeViewModel(@NonNull Application application) {
        super(application);
        repository = SavedRecipeRepository.getInstance(application);
    }

    public void fetchAllRecipesFromSQLite() {
        repository.fetchAllRecipesFromSQLite();
    }

    public void deleteRecipe(Recipe recipe) {
        repository.deleteRecipe(recipe);
    }

    public void deleteAllRecipes() {
        repository.deleteAllRecipes();
    }

    public LiveData<List<Recipe>> getAllRecipesFromSQLite() {
        return repository.getAllRecipesFromSQLite();
    }

    public LiveData<Boolean> getIsDeleted() {
        return repository.getIsRecipeDeleted();
    }

    public LiveData<Boolean> getIsAllRecipesDeleted() {
        return repository.getIsAllRecipesDeleted();
    }

}
