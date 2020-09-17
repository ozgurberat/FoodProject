package com.ozgurberat.foodproject.repositories;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ozgurberat.foodproject.requests.ApiService;
import com.ozgurberat.foodproject.requests.ServiceGenerator;
import com.ozgurberat.foodproject.requests.responses.FoodResponse;
import com.ozgurberat.foodproject.requests.responses.RecipeResponse;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class FoodRepository {
    private static final String TAG = "FoodRepository";

    private static FoodRepository instance;
    private MutableLiveData<FoodResponse> foodList = new MutableLiveData<>();
    private MutableLiveData<FoodResponse> queriedFoodList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isTimedOut = new MutableLiveData<>();


    private ApiService api = ServiceGenerator.getApi();
    private CompositeDisposable disposable;

    private FoodRepository() {
    }

    public static synchronized FoodRepository getInstance() {
        if (instance == null) {
            instance = new FoodRepository();
        }
        return instance;
    }

    public void fetchFoods(String category) {
        isLoading.postValue(true);
        isTimedOut.postValue(false);
        disposable = new CompositeDisposable();
        disposable.add(api.getFoods(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(4000, TimeUnit.MILLISECONDS)
                .subscribeWith(new DisposableObserver<FoodResponse>() {
                    @Override
                    public void onNext(FoodResponse f) {
                        if (f.getFoods() != null && f.getFoods().size() > 0) {
                            foodList.postValue(f);
                        }
                        else {
                            foodList.postValue(null); // post null value for empty response or null response
                        }
                        isLoading.postValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: "+e.getLocalizedMessage());
                        foodList.postValue(new FoodResponse(new ArrayList<>())); // post empty value for error or timeout
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

    public void fetchQueriedFoods(String query) {
        isLoading.postValue(true);
        isTimedOut.postValue(false);
        disposable = new CompositeDisposable();
        disposable.add(api.getQueriedFoods(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(4000, TimeUnit.MILLISECONDS)
                .subscribeWith(new DisposableObserver<FoodResponse>() {
                    @Override
                    public void onNext(FoodResponse f) {
                        if (f.getFoods() != null && f.getFoods().size() > 0) {
                            queriedFoodList.postValue(f);
                        }
                        else {
                            Log.d(TAG, "onNext: null or empty.");
                            queriedFoodList.postValue(null);
                        }
                        isLoading.postValue(false);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: "+e.getLocalizedMessage());
                        queriedFoodList.postValue(new FoodResponse(new ArrayList<>())); // post empty value for error or timeout
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


    public MutableLiveData<FoodResponse> getFoodList() {
        return foodList;
    }

    public MutableLiveData<FoodResponse> getQueriedFoods() {
        return queriedFoodList;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> getIsTimedOut() {
        return isTimedOut;
    }



}
