package com.ozgurberat.foodproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ozgurberat.foodproject.model.Category;
import com.ozgurberat.foodproject.repositories.CategoryRepository;
import com.ozgurberat.foodproject.requests.responses.CategoryResponse;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryRepository repository;

    public CategoryViewModel(@NonNull Application application) {
        super(application);
        repository = CategoryRepository.getInstance(application);
    }

    public void fetchCategories() {
        repository.fetchCategories();
    }

    public void fetchCategoriesFromSQLite() { repository.fetchCategoriesFromSQLite(); }

    public LiveData<CategoryResponse> getCategoryList() {
        return repository.getCategoryList();
    }

    public LiveData<Boolean> getIsLoading() {
        return repository.getIsLoading();
    }

    public LiveData<Boolean> getIsTimedOut() {
        return repository.getIsTimedOut();
    }

    public LiveData<List<Category>> getCategoriesFromSQLite() {
        return repository.getCategoriesFromSQLite();
    }

}
