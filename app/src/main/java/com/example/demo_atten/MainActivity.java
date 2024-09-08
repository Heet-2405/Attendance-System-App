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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
//            ab.setIcon(R.drawable.baseline_arrow_back_24); // Set your icon
            ab.setDisplayShowHomeEnabled(true);            // Show home button
            ab.setDisplayUseLogoEnabled(true);             // Use the logo (icon) as the home button
            ab.setTitle("ATTENDANCE SYSTEM");              // Set title
            ab.setDisplayHomeAsUpEnabled(true);            // Enable back button functionality
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back button click
        if (item.getItemId() == android.R.id.home) {
            // Perform the back action, such as finishing the activity
            onBackPressed();  // You can also use finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Student(View view) {
        Intent intent = new Intent(MainActivity.this,Faculty_login.class);
        startActivity(intent);
    }

    public void Faculty(View view) {
        Intent intent = new Intent(MainActivity.this,Student_login.class);
        startActivity(intent);
    }



}