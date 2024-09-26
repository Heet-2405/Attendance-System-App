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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Add_student extends AppCompatActivity {

    ListView classListView;
    ArrayList<String> classList;
    ArrayAdapter<String> adapter;
    String facultyName;
    EditText inputName;
    EditText inputRoll;
    String selectedClassName;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        classListView = findViewById(R.id.classListView);
        db = FirebaseFirestore.getInstance();

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

    // Method to load the class list from Firestore for the logged-in faculty
    private void loadClassList() {
        classList = new ArrayList<>();

        db.collection("Classes")
                .whereEqualTo("facultyName", facultyName)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot result = task.getResult();
                        if (result != null && !result.isEmpty()) {
                            for (QueryDocumentSnapshot document : result) {
                                String className = document.getString("className");
                                String semester = document.getString("semester");
                                classList.add("Class: " + className + ", Semester: " + semester);
                            }
                            adapter = new ArrayAdapter<>(Add_student.this, android.R.layout.simple_list_item_1, classList);
                            classListView.setAdapter(adapter);
                        } else {
                            Log.d("AddStudent", "No classes found for this faculty.");
                            Toast.makeText(getApplicationContext(), "No classes found for this faculty.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.d("AddStudent", "Error getting documents: ", task.getException());
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

        db.collection("Classes")
                .whereEqualTo("facultyName", facultyName)
                .whereEqualTo("className", className)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Get the document ID of the class
                            String documentId = task.getResult().getDocuments().get(0).getId();
                            DocumentReference classRef = db.collection("Classes").document(documentId);

                            // Add the student to the "students" subcollection
                            classRef.collection("students")
                                    .add(new Student(studentName, rollNumber))
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(getApplicationContext(), "Student added to " + className, Toast.LENGTH_LONG).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), "Failed to add student", Toast.LENGTH_LONG).show();
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Class not found", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // Inner class for Student
    public static class Student {
        private String name;
        private String rollNumber;

        public Student() {
            // Default constructor required for Firestore
        }

        public Student(String name, String rollNumber) {
            this.name = name;
            this.rollNumber = rollNumber;
        }

        public String getName() {
            return name;
        }

        public String getRollNumber() {
            return rollNumber;
        }
    }
}
