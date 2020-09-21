package com.ozgurberat.foodproject.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ozgurberat.foodproject.model.Category;
import com.ozgurberat.foodproject.model.Recipe;

@Database(entities = {Category.class, Recipe.class}, version = 9)
public abstract class ApplicationDatabase extends RoomDatabase {

    public abstract CategoryDao getCategoryDao();
    public abstract RecipeDao getRecipeDao();

    private static ApplicationDatabase INSTANCE;

    public static synchronized ApplicationDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    ApplicationDatabase.class,
                    "my_database"
            ).fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
}








