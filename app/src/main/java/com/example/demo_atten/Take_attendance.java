package com.example.demo_atten;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Take_attendance extends AppCompatActivity {

    ListView classListView, studentListView;
    ArrayList<String> classList, studentList;
    ArrayAdapter<String> classAdapter, studentAdapter;
    String facultyName;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        classListView = findViewById(R.id.classListView);
        studentListView = findViewById(R.id.studentListView);
        db = FirebaseFirestore.getInstance();

        // Get the logged-in faculty's name
        SharedPreferences preferences = getSharedPreferences("login_prefs", MODE_PRIVATE);
        facultyName = preferences.getString("facultyName", "");

        loadClassList();

        // Handle class selection
        classListView.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedClass = classList.get(position);
            loadStudentList(selectedClass);
        });
    }

    // Method to load the class list from Firestore
    private void loadClassList() {
        classList = new ArrayList<>();
        db.collection("Classes")
                .whereEqualTo("facultyName", facultyName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot result = task.getResult();
                            if (result != null && !result.isEmpty()) {
                                for (QueryDocumentSnapshot document : result) {
                                    String className = document.getString("className");
                                    String semester = document.getString("semester");
                                    classList.add("Class: " + className + ", Semester: " + semester);
                                }
                                classAdapter = new ArrayAdapter<>(Take_attendance.this, android.R.layout.simple_list_item_1, classList);
                                classListView.setAdapter(classAdapter);
                            } else {
                                Log.d("Take_attendance", "No classes found for this faculty.");
                            }
                        } else {
                            Log.d("Take_attendance", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    // Method to load the student list for the selected class
    // Method to load the student list for the selected class
    private void loadStudentList(String classString) {
        studentList = new ArrayList<>();

        // Extract the actual class name from "Class: Mathematics, Semester: Fall 2024"
        String className = classString.split(",")[0].replace("Class: ", "").trim();

        // Log the extracted class name to verify
        Log.d("Take_attendance", "Extracted class name: " + className);

        // Query Firestore to find the correct class document based on facultyName and className
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

                            // Use the document ID to access the "students" subcollection
                            db.collection("Classes").document(documentId).collection("students")
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            QuerySnapshot result = task1.getResult();
                                            if (result != null && !result.isEmpty()) {
                                                for (DocumentSnapshot document : result) {
                                                    String studentName = document.getString("name");
                                                    String rollNumber = document.getString("rollNumber");
                                                    studentList.add("Name: " + studentName + ", Roll No: " + rollNumber);
                                                }
                                                studentAdapter = new ArrayAdapter<>(Take_attendance.this, android.R.layout.simple_list_item_1, studentList);
                                                studentListView.setAdapter(studentAdapter);
                                            } else {
                                                Log.d("Take_attendance", "No students found for class: " + className);
                                            }
                                        } else {
                                            Log.d("Take_attendance", "Error getting students: ", task1.getException());
                                        }
                                    });
                        } else {
                            Log.d("Take_attendance", "Class not found: " + className);
                        }
                    }
                });
    }

}
