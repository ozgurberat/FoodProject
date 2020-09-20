package com.ozgurberat.foodproject.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.ozgurberat.foodproject.model.Category;
import com.ozgurberat.foodproject.persistence.ApplicationDatabase;
import com.ozgurberat.foodproject.requests.ApiService;
import com.ozgurberat.foodproject.requests.ServiceGenerator;
import com.ozgurberat.foodproject.requests.responses.CategoryResponse;

import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import io.reactivex.subscribers.ResourceSubscriber;

public class CategoryRepository {
    private static final String TAG = "CategoryRepository";

    private static CategoryRepository INSTANCE;
    private MutableLiveData<CategoryResponse> categoryList;
    private MutableLiveData<List<Category>> categoriesFromSQLite;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<Boolean> isTimedOut;

    private final ApiService api;
    private final ApplicationDatabase database;

    private CategoryRepository(Application application) {
        categoryList = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        isTimedOut = new MutableLiveData<>();
        categoriesFromSQLite = new MutableLiveData<>();
        api = ServiceGenerator.getApi();
        database = ApplicationDatabase.getInstance(application.getApplicationContext());
    }

    public static synchronized CategoryRepository getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new CategoryRepository(application);
        }
        return INSTANCE;
    }

    public void fetchCategoriesFromSQLite() {
        isLoading.postValue(true);
        System.out.println("isLoading: "+isLoading.getValue());
        isTimedOut.postValue(false);
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(
                database.getCategoryDao()
                        .getAllCategories()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new ResourceSubscriber<List<Category>>() {
                            @Override
                            public void onNext(List<Category> categories) {
                                if (categories != null && categories.size() > 0) {
                                    categoriesFromSQLite.postValue(categories);
                                }
                                else {
                                    categoriesFromSQLite.postValue(null);
                                    Log.d(TAG, "onNext: categoriesFromSQLite: NULL");
                                }
                                isLoading.postValue(false);
                                System.out.println("isLoading2: "+isLoading.getValue());
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.d(TAG, "onError: fetchCategoriesFromSQLite: "+t.getLocalizedMessage());
                                categoriesFromSQLite.postValue(null);
                                isLoading.postValue(false);
                                isTimedOut.postValue(true);
                                disposable.dispose();
                            }

                            @Override
                            public void onComplete() {
                                isTimedOut.postValue(false);
                                disposable.dispose();
                            }
                        }
                        )
        );
        System.out.println("isLoading3: "+isLoading.getValue());
    }

    public void fetchCategories() {
        isLoading.postValue(true);
        isTimedOut.postValue(false);
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(
                api.getCategories()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .timeout(4000, TimeUnit.MILLISECONDS)
                        .subscribeWith(new ResourceSubscriber<CategoryResponse>() {
                            @Override
                            public void onNext(CategoryResponse categoryResponse) {
                                if (categoryResponse.getCategories() != null && categoryResponse.getCategories().size() > 0) {
                                    categoryList.postValue(categoryResponse);
                                    if (categoriesFromSQLite.getValue() == null) {
                                        storeCategoriesInSQLite(categoryResponse.getCategories());
                                        Log.d(TAG, "onNext: size of categories: "+categoryResponse.getCategories().size());
                                    }
                                }
                                else {
                                    Log.d(TAG, "onNext: categories from API: NULL or EMPTY.");
                                    categoryList.postValue(null);
                                }
                                isLoading.postValue(false);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: fetchCategories: "+e.getLocalizedMessage());
                                categoryList.postValue(null);
                                isLoading.postValue(false);
                                isTimedOut.postValue(true);
                                disposable.dispose();
                            }

                            @Override
                            public void onComplete() {
                                isTimedOut.postValue(false);
                                disposable.dispose();
                            }
                        })
        );
    }


    public void storeCategoriesInSQLite(List<Category> categories) {
        CompositeDisposable disposable = new CompositeDisposable();
        disposable.add(
                database.getCategoryDao()
                    .insertCategories(categories)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableMaybeObserver<List<Long>>() {
                        @Override
                        public void onSuccess(List<Long> longs) {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d(TAG, "onError: storeCategoriesInSQLite: "+e.getLocalizedMessage());
                            disposable.dispose();
                        }

                        @Override
                        public void onComplete() {
                            disposable.dispose();
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

    public MutableLiveData<List<Category>> getCategoriesFromSQLite() {
        return categoriesFromSQLite;
    }
}
