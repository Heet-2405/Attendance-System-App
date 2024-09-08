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

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setIcon(R.drawable.baseline_arrow_back_24);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setTitle("ATTENDANCE SYSTEM");
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
}