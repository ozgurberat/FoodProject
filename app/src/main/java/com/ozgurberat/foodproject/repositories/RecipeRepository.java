package com.ozgurberat.foodproject.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ozgurberat.foodproject.model.Recipe;
import com.ozgurberat.foodproject.persistence.ApplicationDatabase;
import com.ozgurberat.foodproject.requests.ApiService;
import com.ozgurberat.foodproject.requests.ServiceGenerator;
import com.ozgurberat.foodproject.requests.responses.RecipeResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

public class RecipeRepository {
    private static final String TAG = "RecipeRepository";

    private static RecipeRepository INSTANCE;
    private MutableLiveData<RecipeResponse> recipeResponse;
    private MutableLiveData<Recipe> recipeFromSQLite;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<Boolean> isTimedOut;

    private final ApiService api;
    private final ApplicationDatabase database;

    private RecipeRepository(Application application) {
        recipeResponse = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        isTimedOut = new MutableLiveData<>();
        recipeFromSQLite = new MutableLiveData<>();
        api = ServiceGenerator.getApi();
        database = ApplicationDatabase.getInstance(application.getApplicationContext());
    }

    public static synchronized RecipeRepository getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new RecipeRepository(application);
        }
        return INSTANCE;
    }

    public void fetchRecipeFromSQLite(String id) {
        isLoading.postValue(true);
        isTimedOut.postValue(false);
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(
                database.getRecipeDao()
                        .getRecipe(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new ResourceSubscriber<Recipe>() {
                            @Override
                            public void onNext(Recipe recipe) {
                                if (recipe != null) {
                                    recipeFromSQLite.postValue(recipe);
                                }
                                else {
                                    Log.d(TAG, "onNext: fetchRecipeFromSQLite: NULL");
                                    recipeFromSQLite.postValue(null);
                                }
                                isLoading.postValue(false);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.d(TAG, "onError: fetchRecipeFromSQLite: "+t.getLocalizedMessage());
                                recipeFromSQLite.postValue(null);
                                isLoading.postValue(false);
                                disposable.dispose();
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: fetchRecipeFromSQLite: FINISHED");
                                disposable.dispose();
                            }
                        }));
    }

    public void fetchRecipe(String id) {
        isLoading.postValue(true);
        isTimedOut.postValue(false);
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(
                api.getFoodRecipe(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .timeout(4000, TimeUnit.MILLISECONDS)
                        .subscribeWith(new ResourceSubscriber<RecipeResponse>() {
                            @Override
                            public void onNext(RecipeResponse r) {
                                if (r.getRecipe() != null && r.getRecipe().size() == 1) {
                                    recipeResponse.postValue(r);
                                }
                                else {
                                    Log.d(TAG, "onNext: fetchRecipe: NULL.");
                                    recipeResponse.postValue(null);
                                }
                                isLoading.postValue(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: fetchRecipe: "+e.getLocalizedMessage());
                                recipeResponse.postValue(null);
                                isLoading.postValue(false);
                                isTimedOut.postValue(true);
                                disposable.dispose();
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: fetchRecipe: FINISHED");
                                disposable.dispose();
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

    public void triggerGetRecipe(RecipeResponse recipeList) {
        recipeResponse.postValue(recipeList);
    }
}
