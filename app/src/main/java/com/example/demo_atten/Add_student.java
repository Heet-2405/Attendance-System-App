package com.example.demo_atten;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Add_student extends AppCompatActivity {

    ListView classListView;
    ArrayList<String> classList;
    ArrayAdapter<String> adapter;
    String facultyName;
    EditText inputName;
    EditText inputRoll;
    String selectedClassName;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        classListView = findViewById(R.id.classListView);
        databaseReference = FirebaseDatabase.getInstance().getReference("Classes");

        // Get the logged-in faculty's name from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        facultyName = preferences.getString("facultyName", "");

        Log.d("AddStudent", "Logged in Faculty: " + facultyName);

        loadClassList();

        // Handle class selection
        classListView.setOnItemClickListener((adapterView, view, position, id) -> {
            selectedClassName = classList.get(position); // Get selected class name
            showAddStudentDialog();
        });
    }

    // Method to load the class list from Firebase Realtime Database for the logged-in faculty
    private void loadClassList() {
        classList = new ArrayList<>();

        databaseReference.orderByChild("facultyName").equalTo(facultyName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        classList.clear(); // Clear the list before adding new data
                        for (DataSnapshot classSnapshot : dataSnapshot.getChildren()) {
                            String className = classSnapshot.child("className").getValue(String.class);
                            String semester = classSnapshot.child("semester").getValue(String.class);
                            classList.add("Class: " + className + ", Semester: " + semester);
                        }
                        adapter = new ArrayAdapter<>(Add_student.this, android.R.layout.simple_list_item_1, classList);
                        classListView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("AddStudent", "Error retrieving classes: " + databaseError.getMessage());
                        Toast.makeText(getApplicationContext(), "Error retrieving classes.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Show a dialog to input the student's name and roll number
    private void showAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Student");

        // Create a layout for the input fields
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Input field for student name
        inputName = new EditText(this);
        inputName.setHint("Enter student name");
        layout.addView(inputName);

        // Input field for roll number
        inputRoll = new EditText(this);
        inputRoll.setHint("Enter roll number");
        layout.addView(inputRoll);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            String studentName = inputName.getText().toString().trim();
            String rollNumber = inputRoll.getText().toString().trim();

            // Validate the input
            if (!studentName.isEmpty() && !rollNumber.isEmpty()) {
                addStudentToClass(studentName, rollNumber);
            } else {
                Toast.makeText(getApplicationContext(), "Please enter both student name and roll number", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Method to add the student to the selected class
    private void addStudentToClass(String studentName, String rollNumber) {
        // Extract the class name from selectedClassName (e.g., "Class: Mathematics, Semester: Fall 2024")
        String className = selectedClassName.split(",")[0].replace("Class: ", "").trim();

        databaseReference.orderByChild("className").equalTo(className)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot classSnapshot : dataSnapshot.getChildren()) {
                                String classId = classSnapshot.getKey(); // Get the unique class ID

                                // Create student data with attendance counts
                                Map<String, Object> studentData = new HashMap<>();
                                studentData.put("name", studentName);
                                studentData.put("rollNumber", rollNumber);
                                studentData.put("Total_cnt", 0); // Initialize total count to 0
                                studentData.put("attendance_cnt", 0); // Initialize attendance count to 0

                                // Add the student to the "students" node under the specific class
                                assert classId != null;
                                databaseReference.child(classId).child("students").child(rollNumber).setValue(studentData)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(getApplicationContext(), "Student added to " + className, Toast.LENGTH_LONG).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(getApplicationContext(), "Failed to add student", Toast.LENGTH_LONG).show();
                                        });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Class not found", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("AddStudent", "Error adding student: " + databaseError.getMessage());
                    }
                });
    }
}
