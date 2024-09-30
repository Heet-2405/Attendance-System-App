package com.example.demo_atten;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Student_login extends AppCompatActivity
{

    EditText etName,etRollNumber;
    DatabaseReference databaseReference;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);

        etName = findViewById(R.id.editTextName);
        etRollNumber = findViewById(R.id.editTextRollNumber);

        databaseReference = FirebaseDatabase.getInstance().getReference("Classes"); // Adjust if your path is different

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setIcon(R.drawable.baseline_arrow_back_24);
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayUseLogoEnabled(true);
        ab.setTitle("ATTENDANCE SYSTEM");
    }

    public void checkStudent(View view)
    {
        String name = etName.getText().toString().trim();
        String rollNumber = etRollNumber.getText().toString().trim();

        if (name.isEmpty() || rollNumber.isEmpty()) {
            Toast.makeText(this, "Please enter both Name and Roll Number", Toast.LENGTH_SHORT).show();
            return;
        }
        validateStudent(name, rollNumber);
    }

    private void validateStudent(String name, String rollNumber) {
        // Assuming "Classes" is the parent node in Firebase and contains child nodes with class and student data.
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean studentFound = false;

                for (DataSnapshot classSnapshot : snapshot.getChildren()) {
                    DataSnapshot studentsSnapshot = classSnapshot.child("students");

                    for (DataSnapshot studentSnapshot : studentsSnapshot.getChildren()) {
                        String dbName = studentSnapshot.child("name").getValue(String.class);
                        String dbRollNumber = studentSnapshot.child("rollNumber").getValue(String.class);

                        if (dbName != null && dbRollNumber != null && dbName.equals(name) && dbRollNumber.equals(rollNumber)) {
                            studentFound = true;
                            break;
                        }
                    }

                    if (studentFound) break; // No need to search further if student is found
                }

                if (studentFound) {
                    // Student exists, navigate to studentviewattendance.java
                    Intent intent = new Intent(Student_login.this, StudentViewAttendance.class);
                    intent.putExtra("studentName", name);
                    intent.putExtra("rollNumber", rollNumber);
                    startActivity(intent);
                } else {
                    // Student does not exist, show invalid student message
                    Toast.makeText(Student_login.this, "Invalid student", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Student_login.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}