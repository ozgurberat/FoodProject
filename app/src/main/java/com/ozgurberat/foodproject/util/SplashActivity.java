package com.ozgurberat.foodproject.util;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.ozgurberat.foodproject.R;
import com.ozgurberat.foodproject.view.BaseActivity;

public class SplashActivity extends Activity {

    private static final long SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                ImageView imageView = findViewById(R.id.splash_image);
                imageView.setImageAlpha(0);
                imageView.animate().alpha(1f).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        Intent mainIntent = new Intent(SplashActivity.this, BaseActivity.class);
                        startActivity(mainIntent);
                    }
                });
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}