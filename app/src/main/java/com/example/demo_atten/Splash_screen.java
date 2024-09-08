package com.example.demo_atten;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setIcon(R.drawable.baseline_arrow_back_24);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setTitle("ATTENDANCE SYSTEM");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash_screen.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish the splash activity
            }
        }, 2000);

    }


}