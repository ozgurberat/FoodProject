package com.ozgurberat.foodproject.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ozgurberat.foodproject.model.Recipe;
import com.ozgurberat.foodproject.repositories.FoodRepository;
import com.ozgurberat.foodproject.requests.responses.FoodResponse;

import java.util.List;

public class FoodViewModel extends AndroidViewModel {

    private FoodRepository repository;

    public FoodViewModel(@NonNull Application application) {
        super(application);
        repository = FoodRepository.getInstance(application);
    }

    public void fetchFoods(String category) {
        repository.fetchFoods(category);
    }

    public void fetchQueriedFoods(String query) {
        repository.fetchQueriedFoods(query);
    }

    public void fetchRecipeForResult(String mealId) {
        repository.fetchRecipeForResult(mealId);
    }

    public LiveData<FoodResponse> getFoodList() {
        return repository.getFoodList();
    }

    public LiveData<FoodResponse> getQueriedFoods() {
        return repository.getQueriedFoods();
    }

    public LiveData<Boolean> getIsLoading() {
        return repository.getIsLoading();
    }

    public LiveData<Boolean> getIsTimedOut() {
        return repository.getIsTimedOut();
    }

    public LiveData<Recipe> getRecipeFromSQLite() { return repository.getRecipeFromSQLite(); }

}
