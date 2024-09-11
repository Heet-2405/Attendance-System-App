package com.example.demo_atten;

import android.content.SharedPreferences;
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
    String facultyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        // Get the logged-in faculty's name
        SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        facultyName = preferences.getString("facultyName", "");

        etClassName = findViewById(R.id.editTextText6);
        etSemester = findViewById(R.id.editTextText7);
        dbHelper = new Make_class(this);
    }

    public void CreateClass(View view) {
        String className = etClassName.getText().toString().trim();
        String semester = etSemester.getText().toString().trim();

        if (!className.isEmpty() && !semester.isEmpty()) {
            if (dbHelper.addClass(className, semester, facultyName)) { // Pass facultyName
                Toast.makeText(getApplicationContext(), "Class has been Created", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Class Creation Failed", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
        }
    }
}