package com.example.demo_atten;

import android.content.Intent;
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

public class Faculty_login extends AppCompatActivity {

    EditText etName, etPassword;
    DatabaseHelper dbHelper;
    Intent intent_Login;
    Intent intent_Register;
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


        SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // If already logged in, go to Dashboard
            Intent intent = new Intent(Faculty_login.this, Dashboard.class);
            intent.putExtra("name", preferences.getString("facultyName", ""));
            startActivity(intent);
            finish();  // Finish login activity
        }

        etName = findViewById(R.id.editTextText);
        etPassword = findViewById(R.id.editTextText2);
        dbHelper = new DatabaseHelper(this);


    }

    public void Login_success(View view) {

        String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both Name and Password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if faculty exists in database
        if (dbHelper.checkFaculty(name, password)) {
            // Save login status
            SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putString("facultyName", name);
            editor.apply();

            intent_Login = new Intent(Faculty_login.this, Dashboard.class);
            intent_Login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent_Login.putExtra("name", name);
            startActivity(intent_Login);
            finish();  // Finish login activity to prevent going back to it

        } else {
            Toast.makeText(this, "Login failed! Please check your credentials OR Please register first.", Toast.LENGTH_SHORT).show();
        }
    }



    public void Login_fail(View view) {
         intent_Register = new Intent(Faculty_login.this, Register.class);
        startActivity(intent_Register);
        finish();

    }
}
