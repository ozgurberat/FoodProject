package com.ozgurberat.foodproject.viewmodel;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ozgurberat.foodproject.model.Recipe;
import com.ozgurberat.foodproject.repositories.RecipeRepository;
import com.ozgurberat.foodproject.requests.responses.RecipeResponse;

public class RecipeViewModel extends ViewModel {

    private RecipeRepository repository;

    public RecipeViewModel() {
        repository = RecipeRepository.getInstance();
    }

    public void fetchRecipe(String id) {
        repository.fetchRecipe(id);
    }

    public MutableLiveData<RecipeResponse> getRecipe() {
        return repository.getRecipe();
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return repository.getIsLoading();
    }

    public MutableLiveData<Boolean> getIsTimedOut() {
        return repository.getIsTimedOut();
    }


}
