package com.example.demo_atten;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register extends AppCompatActivity {

    EditText etName, etPassword;
    Button register;
    DatabaseHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setIcon(R.drawable.baseline_arrow_back_24);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setTitle("ATTENDANCE SYSTEM");

        etName = findViewById(R.id.editTextText3);
        etPassword = findViewById(R.id.editTextText5);
        dbHelper = new DatabaseHelper(this);

        register = findViewById(R.id.button5);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String password = etPassword.getText().toString();

                // Insert faculty details in the database
                if (dbHelper.registerFaculty(name, password)) {
                    Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, Faculty_login.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Register.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}
