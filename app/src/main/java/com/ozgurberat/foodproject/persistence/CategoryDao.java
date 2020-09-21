package com.ozgurberat.foodproject.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ozgurberat.foodproject.model.Category;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface CategoryDao {

    @Insert(entity = Category.class, onConflict = OnConflictStrategy.REPLACE)
    Maybe<List<Long>> insertCategories(List<Category> categories);

    @Query("SELECT * FROM category_table")
    Flowable<List<Category>> getAllCategories();

}











