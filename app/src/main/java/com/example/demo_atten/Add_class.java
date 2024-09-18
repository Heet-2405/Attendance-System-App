package com.example.demo_atten;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Add_class extends AppCompatActivity {

    EditText etClassName, etSemester;
    FirebaseFirestore firestore;
    String facultyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);  // Set the XML layout

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance();

        // Get the logged-in faculty's name from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        facultyName = preferences.getString("facultyName", "");

        // Link XML elements to Java variables
        etClassName = findViewById(R.id.editTextText6);  // Subject
        etSemester = findViewById(R.id.editTextText7);   // Semester
    }

    // Method called when "Create Class" button is clicked
    public void CreateClass(View view) {
        String className = etClassName.getText().toString().trim();  // Get subject name
        String semester = etSemester.getText().toString().trim();    // Get semester

        // Validate inputs
        if (!className.isEmpty() && !semester.isEmpty()) {
            // Prepare class data to add to Firestore
            Map<String, Object> classData = new HashMap<>();
            classData.put("className", className);
            classData.put("semester", semester);
            classData.put("facultyName", facultyName);

            // Add class data to Firestore under a "Classes" collection
            firestore.collection("Classes")
                    .add(classData)
                    .addOnSuccessListener(documentReference -> {
                        // Show success message
                        Toast.makeText(getApplicationContext(), "Class has been created successfully", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        // Show failure message
                        Toast.makeText(getApplicationContext(), "Failed to create class: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        } else {
            // Show error if fields are not filled
            Toast.makeText(this, "Please fill in both Subject and Semester", Toast.LENGTH_SHORT).show();
        }
    }
}
