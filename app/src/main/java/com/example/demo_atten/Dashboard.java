package com.example.demo_atten;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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