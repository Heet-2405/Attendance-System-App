package com.example.demo_atten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class Dashboard extends AppCompatActivity {
    Intent Logout_intent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout first
        setContentView(R.layout.activity_dashboard);

        // Now retrieve the intent and get the passed data
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String Action_title = name.toUpperCase();



        // Configure the ActionBar after the layout is set
        ActionBar ab = getSupportActionBar();
        if (ab != null) {

            ab.setDisplayShowHomeEnabled(true);
            ab.setDisplayUseLogoEnabled(true);
            ab.setTitle("Hello " + Action_title); // Set the title passed from the previous activity
        }


    }




    public void Add_Class(View view) {
        Intent intent = new Intent(Dashboard.this,Add_class.class);
        startActivity(intent);
    }

    public void Add_Student(View view) {
        Intent intent = new Intent(Dashboard.this,Add_student.class);
        startActivity(intent);
    }

    public void Take_Atten(View view) {
        Intent intent = new Intent(Dashboard.this,Take_attendance.class);
        startActivity(intent);
    }

    public void View_Atten(View view) {
        Intent intent = new Intent(Dashboard.this,View_Attendance.class);
        startActivity(intent);
    }

    public void Logout(View view) {
        SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();  // Clear the login data
        editor.apply();

        // Go back to login screen
         Logout_intent = new Intent(Dashboard.this, Faculty_login.class);
        startActivity(Logout_intent);
        finish();  // Close dashboard activity
    }

}