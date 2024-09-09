package com.example.demo_atten;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Add_class extends AppCompatActivity {

    EditText etClassName, etSemester;
    Make_class dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setIcon(R.drawable.baseline_arrow_back_24);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setTitle("ATTENDANCE SYSTEM");

        // Initialize views
        etClassName = findViewById(R.id.editTextText6);
        etSemester = findViewById(R.id.editTextText7);
        dbHelper = new Make_class(this);
    }

    public void CreateClass(View view) {
        String className = etClassName.getText().toString().trim();
        String semester = etSemester.getText().toString().trim();

        // Add class to the database
        if (!className.isEmpty() && !semester.isEmpty()) {
            if (dbHelper.addClass(className, semester)) {
                Toast.makeText(getApplicationContext(), "Class has been Created", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Class Creation Failed", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
        }
    }
}
