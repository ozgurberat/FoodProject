package com.ozgurberat.foodproject.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ozgurberat.foodproject.requests.ApiService;
import com.ozgurberat.foodproject.requests.ServiceGenerator;
import com.ozgurberat.foodproject.requests.responses.CategoryResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CategoryRepository {
    private static final String TAG = "CategoryRepository";

    private static CategoryRepository instance;
    private MutableLiveData<CategoryResponse> categoryList;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<Boolean> isTimedOut;

    private CompositeDisposable disposable;
    private ApiService api;

    private CategoryRepository() {
        categoryList = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        isTimedOut = new MutableLiveData<>();
        api = ServiceGenerator.getApi();
    }

    public static synchronized CategoryRepository getInstance() {
        if (instance == null) {
            instance = new CategoryRepository();
        }
        return instance;
    }

    public void fetchCategories() {
        isLoading.postValue(true);
        isTimedOut.postValue(false);
        disposable = new CompositeDisposable();
        disposable.add(
                api.getCategories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .timeout(4000, TimeUnit.MILLISECONDS)
                        .subscribeWith(new DisposableObserver<CategoryResponse>() {
                            @Override
                            public void onNext(CategoryResponse c) {
                                if (c.getCategories() != null && c.getCategories().size() > 0) {
                                    categoryList.postValue(c);
                                }
                                else {
                                    Log.d(TAG, "onNext: null or empty.");
                                    categoryList.postValue(null);
                                }
                                isLoading.postValue(false);
                                System.out.println("...");
                            }
                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: "+e.getLocalizedMessage());
                                categoryList.postValue(null);
                                isLoading.postValue(false);
                                isTimedOut.postValue(true);
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: finished.");
                                disposable.dispose();
                                isTimedOut.postValue(false);
                            }
                        })
        );
    }


    public MutableLiveData<CategoryResponse> getCategoryList() {
        return categoryList;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> getIsTimedOut() {
        return isTimedOut;
    }



}
