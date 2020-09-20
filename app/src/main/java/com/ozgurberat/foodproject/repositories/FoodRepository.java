package com.ozgurberat.foodproject.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ozgurberat.foodproject.model.Recipe;
import com.ozgurberat.foodproject.persistence.ApplicationDatabase;
import com.ozgurberat.foodproject.requests.ApiService;
import com.ozgurberat.foodproject.requests.ServiceGenerator;
import com.ozgurberat.foodproject.requests.responses.FoodResponse;
import com.ozgurberat.foodproject.requests.responses.RecipeResponse;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

public class FoodRepository {
    private static final String TAG = "FoodRepository";

    private static FoodRepository INSTANCE;
    private MutableLiveData<FoodResponse> foodList;
    private MutableLiveData<FoodResponse> queriedFoodList;
    private MutableLiveData<Recipe> recipeFromSQLite;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<Boolean> isTimedOut;

    private final ApiService api;
    private final ApplicationDatabase database;

    private FoodRepository(Application application) {
        foodList = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        isTimedOut = new MutableLiveData<>();
        queriedFoodList = new MutableLiveData<>();
        recipeFromSQLite = new MutableLiveData<>();
        api = ServiceGenerator.getApi();
        database = ApplicationDatabase.getInstance(application.getApplicationContext());
    }

    public static synchronized FoodRepository getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new FoodRepository(application);
        }
        return INSTANCE;
    }

    public void fetchRecipeForResult(String mealId) {
        isLoading.postValue(true);
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(
                api.getFoodRecipe(mealId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .delay(2000, TimeUnit.MILLISECONDS)
                    .timeout(5000, TimeUnit.MILLISECONDS)
                    .subscribeWith(new ResourceSubscriber<RecipeResponse>() {
                        @Override
                        public void onNext(RecipeResponse recipeResponse) {
                            if (recipeResponse != null && recipeResponse.getRecipe() != null) {
                                if (recipeResponse.getRecipe().get(0) != null) {
                                    storeRecipeInSQLite(recipeResponse.getRecipe().get(0));
                                    recipeFromSQLite.postValue(recipeResponse.getRecipe().get(0));
                                }
                                else
                                    recipeFromSQLite.postValue(null);
                            }
                            else
                                recipeFromSQLite.postValue(null);
                            isLoading.postValue(false);
                        }

                        @Override
                        public void onError(Throwable t) {
                            Log.d(TAG, "onError: fetchRecipeForResult: "+t.getLocalizedMessage());
                            recipeFromSQLite.postValue(null);
                            isLoading.postValue(false);
                            disposable.dispose();
                        }

                        @Override
                        public void onComplete() {
                            disposable.dispose();
                        }
                    })
        );
    }

    private void storeRecipeInSQLite(Recipe recipe) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                database.getRecipeDao()
                        .insertRecipe(recipe)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableMaybeObserver<Long>() {
                            @Override
                            public void onSuccess(Long aLong) {
                                System.out.println("row ID : "+aLong);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.d(TAG, "onError: storeRecipeInSQLite: "+t.getLocalizedMessage());
                                compositeDisposable.dispose();
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: SAVED");
                                compositeDisposable.dispose();
                            }
                        })
        );
    }

    public void fetchFoods(String category) {
        isLoading.postValue(true);
        isTimedOut.postValue(false);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                api.getFoods(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(4000, TimeUnit.MILLISECONDS)
                .subscribeWith(new ResourceSubscriber<FoodResponse>() {
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
                        Log.d(TAG, "onError: fetchFoods: "+e.getLocalizedMessage());
                        foodList.postValue(new FoodResponse(new ArrayList<>())); // post empty value for error or timeout
                        isLoading.postValue(false);
                        isTimedOut.postValue(true);
                        compositeDisposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        isTimedOut.postValue(false);
                        compositeDisposable.dispose();
                    }
                })
        );
    }

    public void fetchQueriedFoods(String query) {
        isLoading.postValue(true);
        isTimedOut.postValue(false);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(api.getQueriedFoods(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(4000, TimeUnit.MILLISECONDS)
                .subscribeWith(new ResourceSubscriber<FoodResponse>() {
                    @Override
                    public void onNext(FoodResponse f) {
                        if (f.getFoods() != null && f.getFoods().size() > 0) {
                            queriedFoodList.postValue(f);
                        }
                        else {
                            Log.d(TAG, "onNext: fetchQueriedFoods: NULL or EMPTY.");
                            queriedFoodList.postValue(null);
                        }
                        isLoading.postValue(false);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: fetchQueriedFoods: "+e.getLocalizedMessage());
                        queriedFoodList.postValue(new FoodResponse(new ArrayList<>())); // post empty value for error or timeout
                        isLoading.postValue(false);
                        isTimedOut.postValue(true);
                        compositeDisposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        isTimedOut.postValue(false);
                        compositeDisposable.dispose();
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

    public MutableLiveData<Recipe> getRecipeFromSQLite() { return recipeFromSQLite; }

}
