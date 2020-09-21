package com.ozgurberat.foodproject.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ozgurberat.foodproject.model.Recipe;
import com.ozgurberat.foodproject.persistence.ApplicationDatabase;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

public class SavedRecipeRepository {
    private static final String TAG = "SavedRecipeRepository";

    private static SavedRecipeRepository INSTANCE;
    private MutableLiveData<List<Recipe>> allRecipesFromSQLite;
    private MutableLiveData<Boolean> isRecipeDeleted;
    private MutableLiveData<Boolean> isAllRecipesDeleted;

    private final ApplicationDatabase database;

    private SavedRecipeRepository(Application application) {
        allRecipesFromSQLite = new MutableLiveData<>();
        isRecipeDeleted = new MutableLiveData<>();
        isAllRecipesDeleted = new MutableLiveData<>();
        database = ApplicationDatabase.getInstance(application.getApplicationContext());
    }

    public static synchronized SavedRecipeRepository getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new SavedRecipeRepository(application);
        }
        return INSTANCE;
    }

    public void fetchAllRecipesFromSQLite() {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(
                database.getRecipeDao()
                        .getAllRecipes()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new ResourceSubscriber<List<Recipe>>() {
                            @Override
                            public void onNext(List<Recipe> recipes) {
                                if (recipes != null) {
                                    allRecipesFromSQLite.postValue(recipes);
                                }
                                else {
                                    Log.d(TAG, "onNext: fetchAllRecipesFromSQLite: NULL.");
                                    allRecipesFromSQLite.postValue(null);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.d(TAG, "onError: fetchAllRecipesFromSQLite: "+t.getLocalizedMessage());
                                allRecipesFromSQLite.postValue(null);
                                disposable.dispose();
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: fetchAllRecipesFromSQLite: FINISHED");
                                disposable.dispose();
                            }
                        })
        );
    }

    public void deleteAllRecipes() {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(
                database.getRecipeDao()
                    .deleteAllRecipes()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableMaybeObserver<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            Log.d(TAG, "onSuccess: DELETED ROWS: "+integer);
                            if (integer > 0) {
                                isAllRecipesDeleted.postValue(true);
                            }
                            else {
                                isAllRecipesDeleted.postValue(false);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            isAllRecipesDeleted.postValue(false);
                            disposable.dispose();
                        }

                        @Override
                        public void onComplete() {
                            disposable.dispose();
                        }
                    })
        );
    }

    public void deleteRecipe(Recipe recipe) {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(
                database.getRecipeDao()
                    .deleteRecipe(recipe)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableMaybeObserver<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            Log.d(TAG, "onSuccess: DELETED ROWS: "+integer);
                            isRecipeDeleted.postValue(true);
                        }

                        @Override
                        public void onError(Throwable e) {
                            disposable.dispose();
                            isRecipeDeleted.postValue(false);
                        }

                        @Override
                        public void onComplete() {
                            disposable.dispose();
                        }
                    })
        );
    }

    public MutableLiveData<List<Recipe>> getAllRecipesFromSQLite() {
        return allRecipesFromSQLite;
    }

    public MutableLiveData<Boolean> getIsRecipeDeleted() {
        return isRecipeDeleted;
    }

    public MutableLiveData<Boolean> getIsAllRecipesDeleted() {
        return isAllRecipesDeleted;
    }

}
