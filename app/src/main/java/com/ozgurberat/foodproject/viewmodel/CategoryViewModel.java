package com.ozgurberat.foodproject.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ozgurberat.foodproject.repositories.CategoryRepository;
import com.ozgurberat.foodproject.requests.responses.CategoryResponse;

public class CategoryViewModel extends ViewModel {

    private CategoryRepository repository;

    public CategoryViewModel() {
        repository = CategoryRepository.getInstance();
    }

    public void fetchCategories() {
        repository.fetchCategories();
    }

    public LiveData<CategoryResponse> getCategoryList() {
        return repository.getCategoryList();
    }

    public LiveData<Boolean> getIsLoading() {
        return repository.getIsLoading();
    }

    public LiveData<Boolean> getIsTimedOut() {
        return repository.getIsTimedOut();
    }
}
