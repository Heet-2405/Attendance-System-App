package com.example.demo_atten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Faculty_login extends AppCompatActivity {

    EditText etName, etPassword;
    FirebaseAuth auth;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_login2);

        auth = FirebaseAuth.getInstance();
        preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setIcon(R.drawable.baseline_arrow_back_24);
            ab.setDisplayShowHomeEnabled(true);
            ab.setDisplayUseLogoEnabled(true);
            ab.setTitle("ATTENDANCE SYSTEM");
        }

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
    }

    public void Login_success(View view) {
        String email = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both Email and Password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login success
                            String facultyName = auth.getCurrentUser().getEmail(); // Getting the logged-in user's email

                            // Saving login state and faculty name in SharedPreferences
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("facultyName", facultyName);
                            editor.apply();

                            Toast.makeText(Faculty_login.this, "Login successful", Toast.LENGTH_SHORT).show();

                            // Navigate to Dashboard
                            Intent intent_login = new Intent(Faculty_login.this, Dashboard.class);
                            intent_login.putExtra("name", facultyName); // Passing the faculty's email to Dashboard
                            startActivity(intent_login);
                            finish();  // Finish login activity
                        } else {
                            // Login failed
                            Toast.makeText(Faculty_login.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void Login_fail(View view) {
        Intent intent_Register = new Intent(Faculty_login.this, Register.class);
        startActivity(intent_Register);
        finish();
    }
}

