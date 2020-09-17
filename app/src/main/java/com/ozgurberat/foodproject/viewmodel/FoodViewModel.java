package com.ozgurberat.foodproject.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ozgurberat.foodproject.repositories.FoodRepository;
import com.ozgurberat.foodproject.requests.responses.FoodResponse;

public class FoodViewModel extends ViewModel {

    private static final String TAG = "FoodViewModel";

    private FoodRepository repository;

    public FoodViewModel() {
        repository = FoodRepository.getInstance();
    }

    public void fetchFoods(String category) {
        Log.d(TAG, "fetchFoods: called in ViewModel.");
        repository.fetchFoods(category);
    }

    public void fetchQueriedFoods(String query) {
        repository.fetchQueriedFoods(query);
    }

    public MutableLiveData<FoodResponse> getFoodList() {
        Log.d(TAG, "observeFoods: called in ViewModel.");
        return repository.getFoodList();
    }

    public MutableLiveData<FoodResponse> getQueriedFoods() {
        return repository.getQueriedFoods();
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return repository.getIsLoading();
    }

    public MutableLiveData<Boolean> getIsTimedOut() {
        return repository.getIsTimedOut();
    }

}
