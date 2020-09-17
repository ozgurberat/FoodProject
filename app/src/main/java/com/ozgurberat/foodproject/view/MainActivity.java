package com.ozgurberat.foodproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ozgurberat.foodproject.R;
import com.ozgurberat.foodproject.viewmodel.CategoryViewModel;
import com.ozgurberat.foodproject.viewmodel.FoodViewModel;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView timeout;

    private CategoryViewModel categoryViewModel;
    private FoodViewModel foodViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.main_progress_bar);
        timeout = findViewById(R.id.main_timeout);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);
        subscribeObservers();

    }

    private void subscribeObservers() {
        categoryViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                System.out.println("aBoolean : "+aBoolean);
                showProgressBar(aBoolean);
            }
        });

        foodViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                showProgressBar(aBoolean);

            }
        });

        categoryViewModel.getIsTimedOut().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                showTimedOutScreen(aBoolean);
            }
        });

        foodViewModel.getIsTimedOut().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                showTimedOutScreen(aBoolean);
            }
        });

    }

    private void showTimedOutScreen(Boolean visibility) {
        timeout.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void showProgressBar(Boolean visibility) {
        progressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);

    }


}







