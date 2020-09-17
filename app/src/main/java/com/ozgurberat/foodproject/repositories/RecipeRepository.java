package com.ozgurberat.foodproject.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ozgurberat.foodproject.model.Recipe;
import com.ozgurberat.foodproject.requests.ApiService;
import com.ozgurberat.foodproject.requests.ServiceGenerator;
import com.ozgurberat.foodproject.requests.responses.RecipeResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RecipeRepository {
    private static final String TAG = "RecipeRepository";

    private static RecipeRepository instance;
    private MutableLiveData<RecipeResponse> recipeResponse = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isTimedOut = new MutableLiveData<>();
    private MutableLiveData<Recipe> recipeFromSQLite = new MutableLiveData<>();

    private ApiService api = ServiceGenerator.getApi();
    private CompositeDisposable disposable;


    private RecipeRepository() { }

    public static synchronized RecipeRepository getInstance() {
        if (instance == null) {
            instance = new RecipeRepository();
        }
        return instance;
    }

    public void fetchRecipe(String id) {
        isLoading.postValue(true);
        isTimedOut.postValue(false);
        disposable = new CompositeDisposable();
        disposable.add(
                api.getFoodRecipe(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .timeout(4000, TimeUnit.MILLISECONDS)
                        .subscribeWith(new DisposableObserver<RecipeResponse>() {
                            @Override
                            public void onNext(RecipeResponse r) {
                                if (r.getRecipe() != null && r.getRecipe().size() == 1) {
                                    recipeResponse.postValue(r);
                                }
                                else {
                                    Log.d(TAG, "onNext: null value.");
                                    recipeResponse.postValue(null);
                                }
                                isLoading.postValue(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: "+e.getLocalizedMessage());
                                recipeResponse.postValue(null);
                                isLoading.postValue(false);
                                isTimedOut.postValue(true);
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: finished.");
                                disposable.dispose();
                                isTimedOut.postValue(false);
                            }
                        }));
    }

    public MutableLiveData<RecipeResponse> getRecipe() {
        return recipeResponse;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Boolean> getIsTimedOut() {
        return isTimedOut;
    }

    public MutableLiveData<Recipe> getRecipeFromSQLite() {
        return recipeFromSQLite;
    }


}
