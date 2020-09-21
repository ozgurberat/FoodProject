package com.ozgurberat.foodproject.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.ozgurberat.foodproject.R;
import com.ozgurberat.foodproject.adapter.SavedRecipeRecyclerAdapter;
import com.ozgurberat.foodproject.model.Recipe;
import com.ozgurberat.foodproject.viewmodel.SavedRecipeViewModel;

import java.util.ArrayList;
import java.util.List;

public class SavedRecipeActivity extends AppCompatActivity implements SavedRecipeRecyclerAdapter.SavedRecipeViewListener {
    private static final String TAG = "SavedRecipeActivity";

    private SavedRecipeViewModel savedRecipeViewModel;
    private SavedRecipeRecyclerAdapter adapter;
    private boolean forAlertDialogAndSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes);

        savedRecipeViewModel = new ViewModelProvider(this).get(SavedRecipeViewModel.class);
        forAlertDialogAndSnackbar = false;
        initRecycler();
        initToolbar();

        savedRecipeViewModel.fetchAllRecipesFromSQLite();
        subscribeObservers();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.saved_recipe_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all) {
            showAlertDialog();
            forAlertDialogAndSnackbar = true;
            return true;
        }
        return false;
    }

    private AlertDialog createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Once delete, you will not be able to undo this action.");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                savedRecipeViewModel.deleteAllRecipes();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }

    private void showAlertDialog() {
        AlertDialog alertDialog = createAlertDialog();
        alertDialog.show();
        Button buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonPositive.setTextColor(ContextCompat.getColor(this.getApplicationContext(), R.color.RED));
        Button buttonNegative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonNegative.setTextColor(ContextCompat.getColor(this.getApplicationContext(), R.color.RED));
    }

    private void subscribeObservers() {
        savedRecipeViewModel.getAllRecipesFromSQLite().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                if (recipes != null) {
                    for (Recipe recipe : recipes) {
                        Log.d(TAG, "onChanged: "+recipe.getMealName() +" / "+recipe.getMealYoutubeLink());
                    }
                    adapter.updateData(recipes);
                }
                else {
                    Log.d(TAG, "onChanged: null recipes from SQLite");
                }

            }
        });

        savedRecipeViewModel.getIsDeleted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    if (forAlertDialogAndSnackbar) {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"SUCCESS! Recipe deleted.", BaseTransientBottomBar.LENGTH_LONG);
                        snackbar.setTextColor(Color.CYAN);
                        snackbar.show();
                    }
                }
                else {
                    if (forAlertDialogAndSnackbar) {
                        Toast.makeText(SavedRecipeActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        savedRecipeViewModel.getIsAllRecipesDeleted().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    if (forAlertDialogAndSnackbar) {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),"SUCCESS! All recipes deleted.", BaseTransientBottomBar.LENGTH_LONG);
                        snackbar.setTextColor(Color.CYAN);
                        snackbar.show();
                    }

                }
                else {
                    if (forAlertDialogAndSnackbar) {
                        Toast.makeText(SavedRecipeActivity.this, "There is no recipe in the cache.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void initRecycler() {
        RecyclerView recyclerView = findViewById(R.id.saved_recipes_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SavedRecipeRecyclerAdapter(this, new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.saved_recipes_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Favorite Recipes");
    }

    @Override
    public void onSavedRecipeClicked(Recipe recipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("intentPath", "intentFromSavedRecipeActivity");
        intent.putExtra("id", recipe.getMealId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClicked(Recipe recipe) {
        savedRecipeViewModel.deleteRecipe(recipe);
        forAlertDialogAndSnackbar = true;
    }
}








