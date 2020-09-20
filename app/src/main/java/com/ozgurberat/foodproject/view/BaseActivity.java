package com.ozgurberat.foodproject.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ozgurberat.foodproject.R;
import com.ozgurberat.foodproject.viewmodel.CategoryViewModel;
import com.ozgurberat.foodproject.viewmodel.FoodViewModel;

public class BaseActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView timeout;

    private CategoryViewModel categoryViewModel;
    private FoodViewModel foodViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        initViews();
        subscribeObservers();

    }

    private void initViews() {
        progressBar = findViewById(R.id.main_progress_bar);
        timeout = findViewById(R.id.main_timeout);
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







