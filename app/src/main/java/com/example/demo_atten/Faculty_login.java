package com.example.demo_atten;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Faculty_login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_faculty_login2);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setIcon(R.drawable.baseline_arrow_back_24);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setTitle("ATTENDANCE SYSTEM");
    }


    public void Login_fail(View view) {
        Intent intent = new Intent(Faculty_login.this,Register.class);
        startActivity(intent);
    }

    public void Login_success(View view) {
        Intent intent = new Intent(Faculty_login.this,Dashboard.class);
        startActivity(intent);
    }
}